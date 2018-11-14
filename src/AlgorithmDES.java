import javax.swing.*;
import java.nio.charset.StandardCharsets;

public class AlgorithmDES
{
	public String Encrypt(String plaintext, Key key)
	{
		Block[] blocks = divBlocks(plaintext);

		for(Block block : blocks)
		{
			// Initial permutation
			permutationStart(block);

			// Divide main block into two halves (32-bit)
			Block32 leftBlock32 = new Block32();
			Block32 rightBlock32 = new Block32();

			for(int i = 0; i < 4; i++)
			{
				leftBlock32.bits[i] = block.bits[i];
				rightBlock32.bits[i] = block.bits[i + 4];
			}

			// Erase parity bits from key (64-bit to 56-bit)
			Block56 key56 = permutationPC1(key.getKeyAsBlock());

			// Divide key block into two halves (28-bit, but using 32-bit block
			//  and ignoring 4 last)
			Block32 leftKey = new Block32();
			Block32 rightKey = new Block32();

			for(int i = 0; i < 56; i++)
				if(i < 28)
					leftKey.setBit(i, key56.getBit(i));

				else
					rightKey.setBit(i - 28, key56.getBit(i));

			// Feistel functions (16 iterations)
			for(int i = 0; i < 16; i++)
			{
				// Copy of right block
				Block32 copyRightBlock32 = new Block32();

				for(int k = 0; k < 4; k++)
					copyRightBlock32.bits[k] = rightBlock32.bits[k];

				// Move key bits left (1 or 2 bits accordingly)
				for(int j = 0; j < ((i == 0 || i == 1 || i == 8 || i == 15) ? 1 : 2); j++)
				{
					moveBitsLeft28(leftKey);
					moveBitsLeft28(rightKey);
				}

				// Choose 48 bits from already moved key
				Block48 key48 = permutationPC2(leftKey, rightKey);

				// Expand data block from 32 bits to 48 bits
				Block48 rightBlock48 = permutationExpandTo48bits(rightBlock32);

				// XOR right data block with 48-bit key
				for(int k = 0; k < 6; k++)
					rightBlock48.bits[k] ^= key48.bits[k];

				// Divide data into 6-bit blocks
				Block8[] block6Table = new Block8[8];
				for(int k = 0; k < 8; k++)
					block6Table[k] = new Block8();

				for(int q = 0; q < 48; q++)
					block6Table[q / 6].setBit(q % 6, rightBlock48.getBit(q));

				Block8[] block4Table = new Block8[8];
				for(int k = 0; k < 8; k++)
					block4Table[k] = new Block8();

				for(int q = 0; q < 32; q++)
					block4Table[q / 4] = sBox(q / 4, block6Table[q / 4]);

				// Merge data from S-Boxes into 32-bit right main block
				for(int q = 0; q < 32; q++)
					rightBlock32.setBit(q, block4Table[q / 4].getBit((q % 4) + 4));

				// Permutation P-Blocks
				rightBlock32 = permutationPBlock(rightBlock32);

				// XOR left and right halves
				for(int k = 0; k < 4; k++)
					leftBlock32.bits[k] ^= rightBlock32.bits[k];

				// Swap left and right blocks
				for(int k = 0; k < 4; k++)
				{
					rightBlock32.bits[k] = leftBlock32.bits[k];
					leftBlock32.bits[k] = copyRightBlock32.bits[k];
				}
			}

			// Merge 32-bit data halves
			for(int i = 0; i < 32; i++)
			{
				block.setBit(i, leftBlock32.getBit(i));
				block.setBit(i + 32, rightBlock32.getBit(i));
			}

			// Final permutation
			permutationFinal(block);
		}

		// Merge all blocks into final String
		StringBuilder cipherText = new StringBuilder();

		for(int i = 0; i < blocks.length; i++)
			cipherText.append(new String(blocks[i].bits, StandardCharsets.UTF_8));

		return cipherText.toString();
	}

	public String Decrypt(String ciphertext, Key key)
	{
		return ciphertext;
	}




	Block[] divBlocks(String text)
	{
		int k = 0;
		Block[] blocks = new Block[text.length() / 8 + 1];

		byte[] textToByte = text.getBytes(StandardCharsets.UTF_8).clone();

		for(int i = 0; i < blocks.length; i++)
		{
			blocks[i] = new Block();

			for(int j = 0; j < 8; j++)
				if(k < text.length())
					blocks[i].bits[j] = textToByte[k++];//(byte) text.charAt(k++);
				else
					blocks[i].bits[j] = 0;
		}

		return blocks;
	}

	void permutationStart(Block block)
	{
		Block temp = new Block();
		final byte[] permutationInitialTable = {
		58, 50, 42, 34, 26, 18, 10, 2,
		60, 52, 44, 36, 28, 20, 12, 4,
		62, 54, 46, 38, 30, 22, 14, 6,
		64, 56, 48, 40, 32, 24, 16, 8,
		57, 49, 41, 33, 25, 17, 9,  1,
		59, 51, 43, 35, 27, 19, 11, 3,
		61, 53, 45, 37, 29, 21, 13, 5,
		63, 55, 47, 39, 31, 23, 15, 7
	};
		/*
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
				temp.setBit(i * 8 + j, block.getBit(((i < 4) ? (58 + i * 2) : ((57 + (i - 4) * 2)) - j * 8) - 1));

		*/
		for(int i = 0; i < permutationInitialTable.length; i++)
			temp.setBit(i, block.getBit(permutationInitialTable[i] - 1));
		block.bits = temp.bits.clone();
	}

	Block56 permutationPC1(Block key64)
	{
		Block56 key56 = new Block56();

		final byte[] permutationTablePC1 =
		{
			57, 49, 41, 33, 25, 17, 9,
			1,  58, 50, 42, 34, 26, 18,
			10, 2,  59, 51, 43, 35, 27,
			19, 11, 3,  60, 52, 44, 36,
			63, 55, 47, 39, 31, 23, 15,
			7,  62, 54, 46, 38, 30, 22,
			14, 6,  61, 53, 45, 37, 29,
			21, 13, 5,  28, 20, 12, 4
		};

		for(int i = 0; i < permutationTablePC1.length; i++)
			key56.setBit(i, key64.getBit(permutationTablePC1[i] - 1));

		return key56;
	}

	Block48 permutationPC2(Block32 blockL, Block32 blockR)
	{
		Block48 block48 = new Block48();

		final byte[] permutationTablePC2 =
		{
		14, 17, 11, 24, 1,  5,
		3,  28, 15, 6,  21, 10,
		23, 19, 12, 4,  26, 8,
		16, 7,  27, 20, 13, 2,
		41, 52, 31, 37, 47, 55,
		30, 40, 51, 45, 33, 48,
		44, 49, 39, 56, 34, 53,
		46, 42, 50, 36, 29, 32
		};

		for(int i = 0; i < 48; i++)
		{
			//block48.setBit(i, ((i < 24) ? blockL : blockR).getBit(permutationTablePC2[i] - 1 - ((i < 24) ? 0 : 28)));
			if(i<24)
				block48.setBit(i,blockL.getBit(permutationTablePC2[i]-1));
			else
				block48.setBit(i,blockR.getBit(permutationTablePC2[i]- 25));
		}

		return block48;
	}

	Block48 permutationExpandTo48bits(Block32 block32)
	{
		Block48 block48 = new Block48();

		final byte[] permutationExpansionTable = {
		32, 1,  2,  3,  4,  5,
		4,  5,  6,  7,  8,  9,
		8,  9,  10, 11, 12, 13,
		12, 13, 14, 15, 16, 17,
		16, 17, 18, 19, 20, 21,
		20, 21, 22, 23, 24, 25,
		24, 25, 26, 27, 28, 29,
		28, 29, 30, 31, 32, 1
		};

		for(int i = 0; i < 48; i++)
			block48.setBit(i, block32.getBit(permutationExpansionTable[i] - 1));

		return block48;
	}

	void moveBitsLeft28(Block32 block28)
	{
		boolean orphanBit = block28.getBit(0);

		for(int i = 0; i < 27; i++)
			block28.setBit(i, block28.getBit(i + 1));

		block28.setBit(27, orphanBit);
	}

	Block8 sBox(int n, Block8 input)
	{
		Block8 output = new Block8();

		final byte[][] sBoxesTable = { {
		14, 4,  13, 1,  2,  15, 11, 8,  3,  10, 6,  12, 5,  9,  0,  7,
		0,  15, 7,  4,  14, 2,  13, 1,  10, 6,  12, 11, 9,  5,  3,  8,
		4,  1,  14, 8,  13, 6,  2,  11, 15, 12, 9,  7,  3,  10, 5,  0,
		15, 12, 8,  2,  4,  9,  1,  7,  5,  11, 3,  14, 10, 0,  6,  13
	}, {
		15, 1,  8,  14, 6,  11, 3,  4,  9,  7,  2,  13, 12, 0,  5,  10,
		3,  13, 4,  7,  15, 2,  8,  14, 12, 0,  1,  10, 6,  9,  11, 5,
		0,  14, 7,  11, 10, 4,  13, 1,  5,  8,  12, 6,  9,  3,  2,  15,
		13, 8,  10, 1,  3,  15, 4,  2,  11, 6,  7,  12, 0,  5,  14, 9
	}, {
		10, 0,  9,  14, 6,  3,  15, 5,  1,  13, 12, 7,  11, 4,  2,  8,
		13, 7,  0,  9,  3,  4,  6,  10, 2,  8,  5,  14, 12, 11, 15, 1,
		13, 6,  4,  9,  8,  15, 3,  0,  11, 1,  2,  12, 5,  10, 14, 7,
		1,  10, 13, 0,  6,  9,  8,  7,  4,  15, 14, 3,  11, 5,  2,  12
	}, {
		7,  13, 14, 3,  0,  6,  9,  10, 1,  2,  8,  5,  11, 12, 4,  15,
		13, 8,  11, 5,  6,  15, 0,  3,  4,  7,  2,  12, 1,  10, 14, 9,
		10, 6,  9,  0,  12, 11, 7,  13, 15, 1,  3,  14, 5,  2,  8,  4,
		3,  15, 0,  6,  10, 1,  13, 8,  9,  4,  5,  11, 12, 7,  2,  14
	}, {
		2,  12, 4,  1,  7,  10, 11, 6,  8,  5,  3,  15, 13, 0,  14, 9,
		14, 11, 2,  12, 4,  7,  13, 1,  5,  0,  15, 10, 3,  9,  8,  6,
		4,  2,  1,  11, 10, 13, 7,  8,  15, 9,  12, 5,  6,  3,  0,  14,
		11, 8,  12, 7,  1,  14, 2,  13, 6,  15, 0,  9,  10, 4,  5,  3
	}, {
		12, 1,  10, 15, 9,  2,  6,  8,  0,  13, 3,  4,  14, 7,  5,  11,
		10, 15, 4,  2,  7,  12, 9,  5,  6,  1,  13, 14, 0,  11, 3,  8,
		9,  14, 15, 5,  2,  8,  12, 3,  7,  0,  4,  10, 1,  13, 11, 6,
		4,  3,  2,  12, 9,  5,  15, 10, 11, 14, 1,  7,  6,  0,  8,  13
	}, {
		4,  11, 2,  14, 15, 0,  8,  13, 3,  12, 9,  7,  5,  10, 6,  1,
		13, 0,  11, 7,  4,  9,  1,  10, 14, 3,  5,  12, 2,  15, 8,  6,
		1,  4,  11, 13, 12, 3,  7,  14, 10, 15, 6,  8,  0,  5,  9,  2,
		6,  11, 13, 8,  1,  4,  10, 7,  9,  5,  0,  15, 14, 2,  3,  12
	}, {
		13, 2,  8,  4,  6,  15, 11, 1,  10, 9,  3,  14, 5,  0,  12, 7,
		1,  15, 13, 8,  10, 3,  7,  4,  12, 5,  6,  11, 0,  14, 9,  2,
		7,  11, 4,  1,  9,  12, 14, 2,  0,  6,  10, 13, 15, 3,  5,  8,
		2,  1,  14, 7,  4,  10, 8,  13, 15, 12, 9,  0,  3,  5,  6,  11
	} };

		// Get row and column number [XXXXXX]XX
		int rowNumber = (input.getBit(0) ? 1 : 0) * 2 + (input.getBit(5) ? 1 : 0);
		int columnNumber = (input.getBit(1) ? 1 : 0) * 8 + (input.getBit(2) ? 1 : 0) * 4 + (input.getBit(3) ? 1 : 0) * 2 + (input.getBit(4) ? 1 : 0);

		// Get number from n-th S-Box [XXXX]XXXX
		byte outputNumber = sBoxesTable[n][rowNumber * 16 + columnNumber];

	/*	0000[1100]   12
		11000000   3     -> setbit 00100000
		4*/

		//for(int i = 0; i < 4; i++)
			//output.bits[0].setBit(i, (((outputNumber & (1 << (4 - i)))) != 0) ? true : false))
		output.bits[0] = outputNumber;



	//	[0010]0000

		return output;
	}

	Block32 permutationPBlock (Block32 block32)
	{
			Block32 output = new Block32();

			final byte[] permutationPBlockTable =	{
			16, 7,  20, 21,
			29, 12, 28, 17,
			1,  15, 23, 26,
			5,  18, 31, 10,
			2,  8,  24, 14,
			32, 27, 3,  9,
			19, 13, 30, 6,
			22, 11, 4,  25
			};

			for(int i = 0; i < 32; i++)
				output.setBit(i, block32.getBit(permutationPBlockTable[i] - 1));

			return output;
	}

	void permutationFinal (Block block)
	{
		Block output = new Block();

		final byte[] permutationFinalTable =
		{
		40, 8, 48, 16, 56, 24, 64, 32,
		39, 7, 47, 15, 55, 23, 63, 31,
		38, 6, 46, 14, 54, 22, 62, 30,
		37, 5, 45, 13, 53, 21, 61, 29,
		36, 4, 44, 12, 52, 20, 60, 28,
		35, 3, 43, 11, 51, 19, 59, 27,
		34, 2, 42, 10, 50, 18, 58, 26,
		33, 1, 41, 9, 49, 17, 57, 25
		};

		for(int i = 0; i < 64; i++)
			output.setBit(i, block.getBit(permutationFinalTable[i] - 1));

		block.bits = output.bits.clone();
	}
}
 class Block
{
	byte[] bits;

	public Block()
	{
		bits = new byte[8];
	}
	public Block(int numberOfBytes)
	{
		bits = new byte[numberOfBytes];
	}
	public Block(byte[] bytes)
	{
		bits = bytes.clone();
	}

	boolean getBit(int position)
	{
		if((bits[position / 8] & (1 << (7 - (position % 8)))) != 0)
			return true;
		else
			return false;

	}
	void setBit(int position, boolean bitValue)
	{
		if(bitValue)
			bits[position / 8] |= (1 << (7 - (position % 8)));
		else
			bits[position / 8] &= ~(1 << (7 - (position % 8)));
	}
}

 class Block32
			extends Block
{
	public Block32()
	{
		super(4);
	}
}

 class Block48
			extends Block
{
	public Block48()
	{
		super(6);
	}
}

 class Block56
			extends Block
{
	public Block56()
	{
		super(7);
	}
}

class Block8
		 extends Block
{
	public Block8()
  {
 	 super(1);
  }
}
