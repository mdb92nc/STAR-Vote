package crypto.adder;

import sexpression.ASExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an election.
 *
 * @author David Walluck
 * @version $LastChangedRevision$ $LastChangedDate$
 * @since 0.0.1
 */
public class Election {

    /** The public key used to encrypt and tally */
    private PublicKey publicKey;

    /** The List of all Votes cast in this Election */
    private List<Vote> votes;

    /** The List of all candidates in this Election (race) */
    private List<ASExpression> choices;

    /**
     * Creates a new election.
     *
     * @param publicKey         the public key
     */
    public Election(PublicKey publicKey, List<ASExpression> choices) {
        this.publicKey = publicKey;
        this.votes = new ArrayList<>();
        this.choices = choices;
    }

    /**
     * Gets the votes of this election.
     *
     * @return          the votes
     */
    public List<Vote> getVotes() {
        return votes;
    }

    /**
     * Casts the given vote in this election.
     *
     * @param vote      the vote
     */
    public void castVote(Vote vote) {
        votes.add(vote);
    }

    /**
     * Sums the votes cast in this election.
     * This is the product of the votes modulo <tt>p</tt>).
     *
     * @return          a vote representing the total of the given list of votes
     */
    public Vote sumVotes() {

        /* Pull out the first vote */
        Vote v = votes.get(0);

        List<ElgamalCiphertext> initList = new ArrayList<>();

        /*
          Load up the initList with new ciphertexts only from the first Vote in the Election
          This specific vote will be a query used to calculate the total number of votes.
        */
        for (ElgamalCiphertext ignored : v.getCipherList()) {
            ElgamalCiphertext ciphertext = new ElgamalCiphertext(AdderInteger.ONE, AdderInteger.ONE, publicKey.getP());
            initList.add(ciphertext);
        }

        VoteProof totalProof = new VoteProof(new MembershipProof(), new ArrayList<MembershipProof>());

        /* Create a single ballot from the initList */
        Vote total = new Vote(initList, choices, totalProof);
        Vote total2 = v;

        /* Homomorphically tally the encrypted votes */
        for (Vote vote : votes)
            total = vote.multiply(total);

        for (int i=1; i<votes.size(); i++)
            total2 = votes.get(i).multiply(total2);

        List<AdderInteger> choices = new ArrayList<>();

        choices.add(AdderInteger.ONE);
        choices.add(AdderInteger.ZERO);
        choices.add(AdderInteger.ZERO);
        choices.add(AdderInteger.ZERO);
        choices.add(AdderInteger.ZERO);

        System.out.println("Testing single vote sumproof");
        /* Compute and verify the vote proof */
        total.computeSumProof(votes.size(), publicKey);

        /* TODO: Note that this won't function properly because it expects the ciphertexts to be 0/1 */
        System.out.println("Verfied: " + total.verifyVoteProof(publicKey, 0, votes.size()));
        System.out.println("-----------------");


        System.out.println("Testing single vote no sumproof: ");
        VoteProof vp = new VoteProof();
        vp.compute(total, publicKey, choices, 0, 1);
        System.out.println("Verfied: " + total.verifyVoteProof(publicKey, 0 ,1));
        System.out.println("-----------------");

        System.out.println("Testing single vote no sumproof2: ");
        VoteProof vp2 = new VoteProof();
        vp2.compute(total2, publicKey, choices, 0, 1);
        System.out.println("Verfied: " + total2.verifyVoteProof(publicKey, 0 ,1));
        System.out.println("-----------------");




        return total;
    }

    /**
     * Gets the final sum given the partial sums, the coefficients, the vote
     * representing the sum, and the master public key.
     * TODO make this less stupid List<List<>> and LaGrange coefficients, among others
     *
     * @param  partialSums      the partial sums (the list of products of the encrypted
     *                          votes' h^y values wrapped in an additional list)
     *
     * @param  sum              the sum
     *
     * @param  masterKey        the master public key
     *
     * @return                  the final vote tally
     */
    public List<AdderInteger> getFinalSum(List<AdderInteger> partialSums, Vote sum, PublicKey masterKey) {

        /*

    	  Adder encrypt is of m (public initial g, p, h) [inferred from code]
    	                    m = {0, 1}
    	                    g' = g^r = g^y
    	                    h' = h^r * f^m = h^y * m'

    	  Quick decrypt (given r) [puzzled out by Kevin Montrose]
    	                    confirm g^r = g'
    	                    m' = (h' / (h^r)) = h' / h^y
    	                    if(m' == f) m = 1
    	                    if(m' == 1) m = 0

    	*/

        /* Get relevant key data */
        AdderInteger q = masterKey.getQ();
        AdderInteger f = masterKey.getF();

        List<AdderInteger> coeffs = new ArrayList<>();
        coeffs.add(AdderInteger.ZERO);

        /* Extract the ciphertexts */
        List<ElgamalCiphertext> cipherList = sum.getCipherList();

        /* Figure out how many ciphertexts there are */
        int csize = cipherList.size();

        List<AdderInteger> results      = new ArrayList<>();

        /* For each cipher (i.e. for each candidate) */
        for (int i = 0; i < csize; i++) {


            /* Pull out the ith partial sum (equals h^y) */
            AdderInteger product = partialSums.get(i);

            /* Get the public value from the ith ciphertext (encrypted sum for ith candidate) (bigH = h' = h^y * f^m) (bigG = g^y) */
            AdderInteger bigH = (cipherList.get(i)).getH();

            /* Divide h' / h^y = f^m, where m = total number of votes for a candidate */
            AdderInteger target = bigH.divide(product);

            /* Indicates if we have successfully resolved the ciphertext */
            boolean gotResult = false;

            AdderInteger j = null;

            /* Possible total number of votes for a candidate */
            int numVotes = votes.size();

            /* Iterate over the number of votes to try to guess n */
            for (int k = 0; k <= numVotes; k++) {

                /* Create a guess */
                j = new AdderInteger(k, q);

                System.out.println("DOES " + f.pow(j) + " equal " + target + "?");

                /* Check the guess and get out when found */
                if (f.pow(j).equals(target)) {
                    gotResult = true;
                    break;
                }
            }

            /* Keep track of found result, otherwise error */
            if (gotResult) results.add(j);
            else throw new SearchSpaceExhaustedException("Error searching for " + target);
        }

        return results;
    }
}
