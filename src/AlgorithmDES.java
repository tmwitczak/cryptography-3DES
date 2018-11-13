import javax.swing.*;

public class AlgorithmDES
{
	public String Encrypt(String plaintext, Key key)
	{
		/*Block[] blocks = divBlocks(plaintext)*/

		/*for(Block block:blocks)
		{

		}*/
		return plaintext;
	}
	public String Decrypt(String ciphertext, Key key)
	{
		return ciphertext;
	}

	private class Block
	{
		byte[] bits = new byte[8];
		boolean getBit(int position)
		{
			if((bits[position/8] & (1<<(8-(position%8))))!=0)
				return true;
			else
				return false;

		}
		void setBit(int position, boolean bitValue)
		{
			if(bitValue)
				bits[position/8] |= (1<<(8-(position%8)));
			else
				bits[position/8] &= ~(1<<(8-(position%8)));
		}
	}

	Block[] divBlocks(String text)
	{
		int k = 0;
		Block[] blocks = new Block[(text.length()-1)/8 + 1];
		//JOptionPane.showMessageDialog(null,((text.length()-1)/8 + 1)+"","",JOptionPane.OK_OPTION);
		for(int i=0;i< blocks.length;i++)
		{
			for(int j=0;j<8;j++)
			{
				if(k<text.length()-1)
				{
					blocks[i].bits[j] = (byte) text.charAt(k++);
				}
				else
				{
					blocks[i].bits[j] = 0;
				}
			}
		}
		return blocks;
	}


}
