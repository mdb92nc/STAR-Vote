/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
 *  Security in the Information Society (RISIS), E-Voting Group (EVG)
 *  Quellgasse 21, CH-2501 Biel, Switzerland
 *
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 *
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *  2. Licensees holding valid commercial licenses for UniCrypt may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and Bern University of Applied Sciences (BFH), Research Institute for
 *   Security in the Information Society (RISIS), E-Voting Group (EVG)
 *   Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package lib.unicrypt.src.main.java.ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import lib.unicrypt.src.main.java.ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import lib.unicrypt.src.main.java.ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import lib.unicrypt.src.main.java.ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import lib.unicrypt.src.main.java.ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.crypto.encoder.BigIntegerConvertFunction;
import lib.unicrypt.src.main.java.ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ByteArrayToStringEncoder
	   extends AbstractEncoder<ByteArrayMonoid, ByteArrayElement, StringMonoid, StringElement> {

	private final StringMonoid stringMonoid;

	protected ByteArrayToStringEncoder(StringMonoid stringMonoid) {
		this.stringMonoid = stringMonoid;
	}

	public StringMonoid getStringMonoid() {
		return this.stringMonoid;
	}

	public StringToByteArrayEncoder getDecoder() {
		return StringToByteArrayEncoder.getInstance(this.getStringMonoid());
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return BigIntegerConvertFunction.getInstance(ByteArrayMonoid.getInstance(), this.getStringMonoid());
	}
//	@Override
//	protected Function abstractGetEncodingFunction() {
//		return ConvertFunction.getInstance(ByteArrayMonoid.getInstance(), this.getStringMonoid());
//	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return BigIntegerConvertFunction.getInstance(this.getStringMonoid(), ByteArrayMonoid.getInstance());
	}

	public static ByteArrayToStringEncoder getInstance(StringMonoid stringMonoid) {
		if (stringMonoid == null) {
			throw new IllegalArgumentException();
		}
		return new ByteArrayToStringEncoder(stringMonoid);
	}

}
