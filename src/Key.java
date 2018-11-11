////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Imports
// > SecureRandom
import java.security.SecureRandom;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 64, 128 or 192 bit key used in 3DES algorithm
public class Key
{
	// Constants
	public enum KeyLength
	{
		SHORT(1 * 64),
		MEDIUM(2 * 64),
		LONG(3 * 64);

		public final int bits;

		KeyLength(int bits)
		{
			this.bits = bits;
		}
	}

	// Methods
	Key(KeyLength length)
	{
		generateRandomKey(length);
	}

	Key(byte[] key) throws Exception
	{
		// Check key size
		if (!checkLength(key))
			throw new Exception("Key must be " + KeyLength.SHORT.bits + ", " + KeyLength.MEDIUM.bits + " or "
					+ KeyLength.LONG.bits + " bits long!");

		// Save key
		this.key = key.clone();
	}

	public String getKeyText()
	{
		return key.toString();
	}

	public String getKeyHexadecimal()
	{
		StringBuilder keyHexadecimal = new StringBuilder();

		for (byte b : key)
		{
			keyHexadecimal.append(String.format("%2s",
					Integer.toHexString(b & 0xFF)).replace(' ', '0'));
		}

		return keyHexadecimal.toString().toUpperCase();
	}

	public String getKeyBinary()
	{
		StringBuilder keyBinary = new StringBuilder();

		for (byte b : key)
		{
			keyBinary.append(String.format("%8s",
					Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
		}

		return keyBinary.toString();
	}

	public int getKeyLength()
	{
		return (key.length * 8);
	}

	private boolean checkLength(byte[] key)
	{
		if (getKeyLength() == KeyLength.SHORT.bits || getKeyLength() == KeyLength.MEDIUM.bits
				|| getKeyLength() == KeyLength.LONG.bits)
			return true;
		else
			return false;
	}

	private void generateRandomKey(KeyLength length)
	{
		SecureRandom randomNumberGenerator = new SecureRandom();

		this.key = new byte[length.bits / 8];
		randomNumberGenerator.nextBytes(this.key);
	}

	// Fields
	private byte[] key;
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class Key56
		extends Key
{
	// Key length constant
	private static final KeyLength KEY_LENGTH = KeyLength.SHORT;

	// Constructors
	public Key56()
	{
		super(KEY_LENGTH);
	}

	public Key56(byte[] key) throws Exception
	{
		// Call parent class constructor
		super(key);

		// Check key size
		if (key.length != KEY_LENGTH.bits)
			throw new Exception("Key must be " + KEY_LENGTH.bits + " bits long!");
	}
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////