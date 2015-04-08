package crypto.adder.test;

import crypto.adder.AdderElgamalCiphertext;
import crypto.adder.AdderInteger;
import crypto.adder.AdderVote;
import crypto.adder.InvalidVoteException;
import junit.framework.TestCase;
import sexpression.ASExpression;
import sexpression.ListExpression;
import sexpression.StringExpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Vote test.
 *
 * @version $LastChangedRevision$ $LastChangedDate$
 * @since 0.0.1
 * @author David Walluck
 */
public class AdderVoteTest extends TestCase {
    /**
     * Constructs a new vote test.
     *
     * @param name the name of the test
     */
    public AdderVoteTest(String name) {
        super(name);
    }

    /**
     * The test.
     */
    public void testString() {
        try {
            AdderVote vote = AdderVote.fromString("p123G135H246");

            assertEquals(new AdderInteger("123"),
                         vote.getCipherList().get(0).getP());
            assertEquals(new AdderInteger("135",
                         vote.getCipherList().get(0).getP()),
                         vote.getCipherList().get(0).getG());
            assertEquals(new AdderInteger("246",
                         vote.getCipherList().get(0).getP()),
                         vote.getCipherList().get(0).getH());
        } catch (InvalidVoteException ive) {
            fail();
        }

        AdderElgamalCiphertext ciphertext1 = new AdderElgamalCiphertext(new AdderInteger("135"),
                              new AdderInteger("246"),
                              new AdderInteger("111"),
                              new AdderInteger("123"));

        List<AdderElgamalCiphertext> cipherList = new ArrayList<>(1);
        cipherList.add(ciphertext1);
        AdderVote vote1 = new AdderVote(cipherList, null);

        assertEquals(new AdderInteger("123"),
                     vote1.getCipherList().get(0).getP());
        assertEquals(new AdderInteger("135",
                     vote1.getCipherList().get(0).getP()),
                     vote1.getCipherList().get(0).getG());
        assertEquals(new AdderInteger("246",
                     vote1.getCipherList().get(0).getP()),
                     vote1.getCipherList().get(0).getH());
        assertEquals(new AdderInteger("111"),
                     vote1.getCipherList().get(0).getR());

        AdderElgamalCiphertext ciphertext2
            = new AdderElgamalCiphertext(new AdderInteger("135"),
                                    new AdderInteger("246"),
                                    new AdderInteger("111"),
                                    new AdderInteger("123"));
        cipherList.clear();
        cipherList.add(ciphertext2);
        AdderVote vote2 = new AdderVote(cipherList, null);

        assertEquals("p123G12H0",
                     vote2.getCipherList().get(0).toString());

        AdderElgamalCiphertext ciphertext3
            = new AdderElgamalCiphertext(new AdderInteger("135"),
                                    new AdderInteger("246"),
                                    new AdderInteger("111"),
                                    new AdderInteger("123"));
        cipherList.clear();
        cipherList.add(ciphertext3);
        AdderVote vote3 = new AdderVote(cipherList, null);

        assertEquals("3b589",
                     vote3.getCipherList().get(0).shortHash());

        try {
            AdderVote.fromString("p123G123");
            fail();
        } catch (InvalidVoteException ignored) {

        }

        try {
            AdderVote.fromString("qGH");
            fail();
        } catch (InvalidVoteException ignored) {

        }

        try {
            AdderVote.fromString("p123H123H123");
            fail();
        } catch (InvalidVoteException ignored) {

        }
        try {
            AdderVote.fromString("p123G123G123");
            fail();
        } catch (InvalidVoteException ignored) {

        }

        try {
            AdderVote.fromString("p123G123H12a");
            fail();
        } catch (InvalidVoteException ignored) {

        }

        try {
            AdderVote.fromString("p123G123H123p123");
            fail();
        } catch (InvalidVoteException ignored) {

        }

        try {
            AdderVote.
                fromString("p1045854189839G17609338705H264688728687 q10458541"
                        + "89839y634399786697y449024425938y211966529664z4986"
                        + "42099355z161654952943z408688746982s50028254339s40"
                        + "5915557693s360340621934c222506129443c148756415697"
                        + "c387464678922");
            fail();
        } catch (InvalidVoteException ignored) {

        }

        try {
            AdderVote.
                  fromString("p1045854189839G17609338705H264688728687 p104585"
                          + "4189839y634399786697y449024425938y211966529664z"
                          + "498642099355z161654952943z408688746982s50028254"
                          + "339s405915557693s360340621934c222506129443c1487"
                          + "56415697c387464678922 xyz");
            fail();
        } catch (InvalidVoteException ignored) {

        }

        try {
            AdderVote vote
                = AdderVote.fromString("p1045854189839G17609338705H264688728687 p1"
                            + "045854189839y634399786697y449024425938y211"
                            + "966529664z498642099355z161654952943z408688"
                            + "746982s50028254339s405915557693s3603406219"
                            + "34c222506129443c148756415697c387464678922"
            );
            assertTrue(vote != null);
            fail();
        } catch (InvalidVoteException ignored) {

        }

        try {
            AdderVote.
                  fromString("p1045854189839G17609338705H264688728687 p104585"
                          + "4189839y634399786697y449024425938y211966529664z"
                          + "498642099355z161654952943z408688746982s50028254"
                          + "339s405915557693s360340621934c222506129443c1487"
                          + "56415697c387464678922 xyz");
            fail();
        } catch (InvalidVoteException ignored) {

        }

        //try {
            //Vote vote
            //    = Vote.fromString("p1045854189839G17609338705H264688728687 p1"
            //                      + "045854189839y634399786697y449024425938y211"
            //                      + "966529664z498642099355z161654952943z408688"
            //                      + "746982s50028254339s405915557693s3603406219"
            //                     + "34c222506129443c148756415697c387464678922"
            //                      );

            //assertEquals("p1045854189839G17609338705H264688728687 p1045854189"
            //             + "839y634399786697y449024425938y211966529664z49864209"
            //             + "9355z161654952943z408688746982s50028254339s40591555"
            //             + "7693s360340621934c222506129443c148756415697c3874646"
            //             + "78922",
            //             ((ElgamalCiphertext) vote.getCipherList().get(0)).toString());
            //assertEquals("p1045854189839y634399786697y449024425938y2119665296"
            //             + "64z498642099355z161654952943z408688746982s500282543"
            //             + "39s405915557693s360340621934c222506129443c148756415"
            //             + "697c387464678922",
            //             ((ElgamalCiphertext) vote.getCipherList().get(0)).
            //                                  getProof().toString());
            //System.out.println("PROOF: " + ((ElgamalCiphertext) vote.getCipherList().get(0)).
            //                                getProof().toString());
        //} catch (InvalidVoteException ive) {
        //    fail();
        //}
    }

    public void testSEXP(){
        AdderElgamalCiphertext ciphertext1 = new AdderElgamalCiphertext(new AdderInteger("135"),
                new AdderInteger("246"),
                new AdderInteger("111"),
                new AdderInteger("123"));

        List<AdderElgamalCiphertext> cipherList = new ArrayList<>(1);
        cipherList.add(ciphertext1);

        ListExpression choices = new ListExpression("B0", "B1", "B2");

        AdderVote myVote= new AdderVote(cipherList, new ArrayList<>(Arrays.asList(choices.getArray())));

        myVote.setRaceTitle("L1");

        List<ASExpression> cList = new ArrayList<>();

        for(AdderElgamalCiphertext text : cipherList)
            cList.add(text.toASE());

        ListExpression vote = new ListExpression(StringExpression.makeString("vote"), new ListExpression(cList));

        ListExpression choicesExp = new ListExpression(StringExpression.makeString("vote-ids"), choices);

        ListExpression titleExp = new ListExpression("title", "L1");

        ListExpression expected = new ListExpression(vote, choicesExp, ListExpression.EMPTY, titleExp);

        /* Test toASE*/
        assertEquals(myVote.toASE().toString(), expected.toString());

        AdderVote newVote = AdderVote.fromASE(expected);

        /* Test fromASE */
        assertEquals(myVote.toASE().toString(), newVote.toASE().toString());
    }
}
