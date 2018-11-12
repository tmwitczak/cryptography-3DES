////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Imports
// > SecureRandom
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

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
		public final int bytes;

		KeyLength(int bits)
		{
			this.bits = bits;
			this.bytes = bits / 8;
		}
	}
	
	public enum Display
	{
		TEXT,
		BIN,
		HEX;
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
	
	Key(String text, KeyLength length, Display display) throws Exception
	{
		switch(display)
		{
		case TEXT:
			if(text.length() != length.bytes)
				throw new Exception("Key must be " + length.bits + " bits long!");
			this.key = text.getBytes(StandardCharsets.UTF_8).clone();
			break;
		case BIN: break;	//TODO: implement key String to binary conversion
		case HEX: break;	//TODO: implement key String to hex conversion
		}
	}

	public String getKeyText()
	{
		return new String(this.key); //TODO: check if conversion is valid
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
class KeyFormatter
{
	static void setFormatter(Key.KeyLength length, Key.Display display, JFormattedTextField textField)
	{
		try
		{
			MaskFormatter maskFormatter = new MaskFormatter();

			StringBuilder mask = new StringBuilder();
			String maskPattern = null;

			switch (display)
			{
				case TEXT:
					maskPattern = "*";
					break;
				case BIN:
					maskPattern = "********";
					maskFormatter.setValidCharacters("01");
					break;
				case HEX:
					maskPattern = "**";
					maskFormatter.setValidCharacters("0123456789abcdefABCDEF");
					break;
			}

			for (int i = 0; i < (length.bits / 8); i++)
			{
				mask.append(maskPattern);

				if (!display.equals(Key.Display.TEXT) && i < (length.bits / 8) - 1)
					mask.append(' ');
			}


			maskFormatter.setMask(mask.toString());

			maskFormatter.setPlaceholderCharacter('\u02FD');

			// Define the factory.

			DefaultFormatterFactory factory = new DefaultFormatterFactory(maskFormatter);

			textField.setFormatterFactory(factory);

		}
		catch (Exception ignored)
		{
		}
	}

	
}


