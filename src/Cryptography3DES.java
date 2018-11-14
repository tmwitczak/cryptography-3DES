////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Imports
// - MigLayout

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// - Swing
//import Key.KeyLength;
// - Awt

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Main class
public class Cryptography3DES
		extends JFrame
{
	// Fields
	// > constant button groups
	private final ButtonGroup groupKeyDisplay = new ButtonGroup();
	private final ButtonGroup groupKeyLength = new ButtonGroup();
	// > used colors
	private final Color color1 = new Color(0x606060);
	private final Color color2 = new Color(0x3C3C3C);
	private final Color color3 = new Color(0xFAFAFA);
	private final Color color4 = new Color(0xC0C0C0);
	private final Color color5 = new Color(0xCC0000);
	// > used fonts
	private final Font font1 = new Font("Courier New", Font.PLAIN, 12);
	private final Font font2 = new Font(Font.SANS_SERIF, Font.BOLD, 12);
	// > key parameters
	private Key.KeyLength keyLength = Key.KeyLength.LONG;
	private Key.Display keyDisplay = Key.Display.TEXT;
	// > key
	private Key key = null;

	AlgorithmDES algorithmDES = new AlgorithmDES();
	/**
	 * Create the frame.
	 */
	public Cryptography3DES()
	{
		// Set frame details
		setTitle("Szyfrowanie/Deszyfrowanie - Algorytm 3DES");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 200, 800, 600);

		// All contents
		JPanel contentPane = new JPanel();
		JLabel labelKey = new JLabel("Klucz");
		JLabel labelKeyInfo = new JLabel("");
		JLabel labelPlainText = new JLabel("Tekst jawny");
		JLabel labelCipherText = new JLabel("Szyfrogram");
		JButton buttonEncrypt = new JButton("S  z  y  f  r  u  j");
		JButton buttonDecrypt = new JButton("D  e  s  z  y  f  r  u  j");
		JFormattedTextField keyTextField = new JFormattedTextField();
		JTextArea plainTextArea = new JTextArea();
		JTextArea cipherTextArea = new JTextArea();
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile = new JMenu("Plik");
		JMenuItem menuItemEncryptFile = new JMenuItem("Szyfruj...");
		JMenuItem menuItemDecryptFile = new JMenuItem("Deszyfruj...");
		JMenu menuKey = new JMenu("Klucz");
		JMenu menuKeyLength = new JMenu("D\u0142ugo\u015B\u0107");
		JRadioButtonMenuItem radioButton64bits = new JRadioButtonMenuItem("64 bity");
		JRadioButtonMenuItem radioButton128bits = new JRadioButtonMenuItem("128 bit\u00F3w");
		JRadioButtonMenuItem radioButton192bits = new JRadioButtonMenuItem("192 bity");
		JMenu menuKeyDisplay = new JMenu("Tryb wpisywania");
		JRadioButtonMenuItem radioButtonText = new JRadioButtonMenuItem("Tekst");
		JRadioButtonMenuItem radioButtonBin = new JRadioButtonMenuItem("Binarnie");
		JRadioButtonMenuItem radioButtonHex = new JRadioButtonMenuItem("Heksadecymalnie");
		JSeparator separator = new JSeparator();
		JMenuItem menuItemGenerate = new JMenuItem("Generuj");
		JMenu menuInfo = new JMenu("Informacje");
		JMenuItem menuItemAuthors = new JMenuItem("Autorzy");

		// Add content elements
		// > Main content pane
		contentPane.setBackground(color2);
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		contentPane.setLayout(new MigLayout("", "[80px][grow]", "[40px][15px][grow][15px][grow][15px][40px]"));
		setContentPane(contentPane);

		// > Labels
		labelKey.setFont(font2);
		labelKey.setForeground(color3);
		contentPane.add(labelKey, "cell 0 0,alignx center");

		labelKeyInfo.setFont(font2);
		labelKeyInfo.setForeground(color5);
		labelKeyInfo.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(labelKeyInfo, "cell 1 1,alignx left,aligny center");

		labelPlainText.setFont(font2);
		labelPlainText.setForeground(color3);
		contentPane.add(labelPlainText, "cell 0 2,alignx center");

		labelCipherText.setFont(font2);
		labelCipherText.setForeground(color3);
		contentPane.add(labelCipherText, "cell 0 4,alignx center");

		// > Buttons
		buttonEncrypt.addActionListener(arg0 ->
		{
				cipherTextArea.setText(algorithmDES.Encrypt(plainTextArea.getText().substring(0, plainTextArea.getText().length() - 1), key));
		});
		buttonEncrypt.setFont(font2);
		buttonEncrypt.setPreferredSize(new Dimension(200, 40));
		buttonEncrypt.setBorder(BorderFactory.createLineBorder(color4, 1));
		buttonEncrypt.setContentAreaFilled(false);
		buttonEncrypt.setForeground(color3);
		buttonEncrypt.setBackground(color1);
		buttonEncrypt.setOpaque(true);
		contentPane.add(buttonEncrypt, "flowx,cell 1 6,growx,aligny center");

		buttonDecrypt.addActionListener(arg0 ->
		{
			plainTextArea.setText(algorithmDES.Decrypt(cipherTextArea.getText().substring(0, cipherTextArea.getText().length() - 1), key));
		});
		buttonDecrypt.setFont(font2);
		buttonDecrypt.setPreferredSize(new Dimension(200, 40));
		buttonDecrypt.setBorder(BorderFactory.createLineBorder(color4, 1));
		buttonDecrypt.setContentAreaFilled(false);
		buttonDecrypt.setForeground(color3);
		buttonDecrypt.setBackground(color1);
		buttonDecrypt.setOpaque(true);
		contentPane.add(buttonDecrypt, "cell 1 6,growx");

		// > Text fields and areas
		KeyFormatter.setFormatter(keyLength, keyDisplay, keyTextField);
		keyTextField.setCaretColor(getForeground());
		keyTextField.setForeground(color3);
		keyTextField.setBackground(color1);
		keyTextField.setFont(font1);
		keyTextField.setToolTipText("klucz");
		keyTextField.setBorder(BorderFactory.createLineBorder(color4, 1));
		contentPane.add(keyTextField, "cell 1 0,grow");
		keyTextField.setColumns(10);

		keyTextField.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e)
			{
				checkKey(e);
			}

			public void removeUpdate(DocumentEvent e)
			{
				checkKey(e);
			}

			public void insertUpdate(DocumentEvent e)
			{
				checkKey(e);
			}

			public void checkKey(DocumentEvent event)
			{
				// Check for any placeholders in key
				try
				{
					String keyText = event.getDocument().getText(event.getDocument().getStartPosition().getOffset(), event.getDocument().getEndPosition().getOffset());
					if (keyText.contains("\u02FD"))
					{
						labelKeyInfo.setText("Wpisz klucz " + keyLength.bits + " bitowy!");
						buttonEncrypt.setEnabled(false);
						buttonDecrypt.setEnabled(false);
						plainTextArea.setEnabled(false);
						menuItemEncryptFile.setEnabled(false);
						menuItemDecryptFile.setEnabled(false);

						key = null;
					}
					else if(keyText.length() > 1 && key == null)
					{
						labelKeyInfo.setText(" ");
						buttonEncrypt.setEnabled(true);
						buttonDecrypt.setEnabled(true);
						plainTextArea.setEnabled(true);
						menuItemEncryptFile.setEnabled(true);
						menuItemDecryptFile.setEnabled(true);

						//labelKeyInfo.setText(keyText.length()+"");

						/*switch (keyDisplay)
						{
							case TEXT:*/
								key = new Key(keyText.substring(0, keyText.length()-1), keyLength, keyDisplay);
								/*break;
							case BIN:
								key = new Key(keyLength);
								break;
							case HEX:
								key = new Key(keyLength);
								break;
						}*/

						//labelKeyInfo.setText(keyText.length()+"aa");
						//plainTextArea.setText(key.getKeyText());
					}
				}
				catch (Exception e)
				{
					//labelKeyInfo.setEnabled(true);
					//JOptionPane.showMessageDialog(null, e.getMessage(), "", JOptionPane.ERROR_MESSAGE);
					//try{String keyText = event.getDocument().getText(event.getDocument().getStartPosition().getOffset(), event.getDocument().getEndPosition().getOffset());
					//labelKeyInfo.setText(keyText.length()+"www");}catch(Exception ea) {labelKeyInfo.setText("fuck");}
					//plainTextArea.setText(e.getMessage());
				}
			}
		});

		plainTextArea.setCaretColor(getForeground());
		plainTextArea.setFont(font1);
		plainTextArea.setForeground(color3);
		plainTextArea.setBackground(color1);
		plainTextArea.setBorder(BorderFactory.createLineBorder(color4, 1));
		contentPane.add(plainTextArea, "cell 1 2,grow");

		cipherTextArea.setCaretColor(getForeground());
		cipherTextArea.setForeground(color3);
		cipherTextArea.setBackground(color1);
		cipherTextArea.setFont(font1);
		cipherTextArea.setBorder(BorderFactory.createLineBorder(color4, 1));
		contentPane.add(cipherTextArea, "cell 1 4,grow");

		// > Menu
		setJMenuBar(menuBar);

		menuBar.add(menuFile);

		menuFile.add(menuItemEncryptFile);

		menuFile.add(menuItemDecryptFile);

		menuBar.add(menuKey);

		menuKey.add(menuKeyLength);

		groupKeyLength.add(radioButton64bits);
		menuKeyLength.add(radioButton64bits);
		radioButton64bits.addActionListener(arg0 ->
		{
			keyLength = Key.KeyLength.SHORT;
			keyTextField.setText("");
			KeyFormatter.setFormatter(keyLength, keyDisplay, keyTextField);
		});

		groupKeyLength.add(radioButton128bits);
		menuKeyLength.add(radioButton128bits);
		radioButton128bits.addActionListener(arg0 ->
		{
			keyLength = Key.KeyLength.MEDIUM;
			keyTextField.setText("");
			KeyFormatter.setFormatter(keyLength, keyDisplay, keyTextField);
		});

		radioButton192bits.setSelected(true);
		groupKeyLength.add(radioButton192bits);
		menuKeyLength.add(radioButton192bits);
		radioButton192bits.addActionListener(arg0 ->
		{
			keyLength = Key.KeyLength.LONG;
			keyTextField.setText("");
			KeyFormatter.setFormatter(keyLength, keyDisplay, keyTextField);
		});

		menuKey.add(menuKeyDisplay);

		menuKeyDisplay.add(radioButtonText);
		radioButtonText.addActionListener(arg0 ->
		{
			keyDisplay = Key.Display.TEXT;
			//String temp = (key != null ? key.getKeyText() : "");
			keyTextField.setText("");
			KeyFormatter.setFormatter(keyLength, keyDisplay, keyTextField);
			//keyTextField.setText(temp);
		});
		radioButtonText.setSelected(true);
		groupKeyDisplay.add(radioButtonText);

		menuKeyDisplay.add(radioButtonBin);
		radioButtonBin.addActionListener(arg0 ->
		{
			keyDisplay = Key.Display.BIN;
			String temp = (key != null ? key.getKeyBinary() : "");
			keyTextField.setText("");
			KeyFormatter.setFormatter(keyLength, keyDisplay, keyTextField);
			keyTextField.setText(temp);
		});
		groupKeyDisplay.add(radioButtonBin);

		menuKeyDisplay.add(radioButtonHex);
		radioButtonHex.addActionListener(arg0 ->
		{
			keyDisplay = Key.Display.HEX;
			String temp = (key != null ? key.getKeyHexadecimal() : "");
			keyTextField.setText("");
			KeyFormatter.setFormatter(keyLength, keyDisplay, keyTextField);
			keyTextField.setText(temp);
		});
		groupKeyDisplay.add(radioButtonHex);

		menuKey.add(separator);

		menuKey.add(menuItemGenerate);
		menuItemGenerate.addActionListener(arg0 ->
		{
			Key randomKey = new Key(keyLength);
			String keyToTextField = null;
			switch (keyDisplay)
			{
				case TEXT:
					keyToTextField = randomKey.getKeyText();
					break;
				case BIN:
					keyToTextField = randomKey.getKeyBinary();
					break;
				case HEX:
					keyToTextField = randomKey.getKeyHexadecimal();
					break;
			}
			keyTextField.setText(keyToTextField);
		});

		menuBar.add(menuInfo);

		menuItemAuthors.addActionListener(arg0 -> JOptionPane.showMessageDialog(null, "Michał Kidawa, 216796\nJakub Szubka, 216901\nTomasz Witczak, 216920", "Autorzy", JOptionPane.PLAIN_MESSAGE));
		menuInfo.add(menuItemAuthors);


		// Events

	}

	// Main
	public static void main(String[] args)
	{
		// Set system feel
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}

		// Set event queue and run application
		EventQueue.invokeLater(() ->
		{
			try
			{
				Cryptography3DES frame = new Cryptography3DES();
				frame.setVisible(true);
			}
			catch (Exception exception)
			{
				exception.printStackTrace();
			}
		});
	}

}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
