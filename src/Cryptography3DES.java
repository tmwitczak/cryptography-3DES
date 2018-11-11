import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Cryptography3DES extends JFrame
{

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Create the frame.
	 */
	public Cryptography3DES()
	{
		setTitle("Szyfrowanie/Deszyfrowanie - Algorytm 3DES");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 823, 628);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnPlik = new JMenu("Plik");
		menuBar.add(mnPlik);

		JMenuItem mntmSzyfrujPlik = new JMenuItem("Szyfruj plik");
		mnPlik.add(mntmSzyfrujPlik);

		JMenuItem mntmDeszyfrujPlik = new JMenuItem("Deszyfruj plik");
		mnPlik.add(mntmDeszyfrujPlik);

		JMenu mnInfo = new JMenu("Informacje");
		menuBar.add(mnInfo);

		JMenuItem mntmAutorzy = new JMenuItem("Autorzy");
		mnInfo.add(mntmAutorzy);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][grow]", "[60.00:60.00:60.00,grow][][][grow][grow][][]"));

		JLabel lblNewLabel = new JLabel("Klucz");
		lblNewLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
		lblNewLabel.setForeground(Color.WHITE);
		contentPane.add(lblNewLabel, "cell 0 0");

		textField = new JTextField();
		textField.setBackground(Color.BLACK);
		textField.setFont(new Font("Verdana", Font.PLAIN, 12));
		textField.setToolTipText("klucz");
		contentPane.add(textField, "cell 1 0,grow");
		textField.setColumns(10);

		JSeparator separator = new JSeparator();
		separator.setBackground(Color.LIGHT_GRAY);
		separator.setForeground(Color.LIGHT_GRAY);
		contentPane.add(separator, "cell 1 2");

		JLabel lblTekstDoZaszyfrowania = new JLabel("Tekst jawny");
		lblTekstDoZaszyfrowania.setFont(new Font("Verdana", Font.PLAIN, 12));
		lblTekstDoZaszyfrowania.setForeground(Color.WHITE);
		contentPane.add(lblTekstDoZaszyfrowania, "cell 0 3");

		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Verdana", Font.PLAIN, 12));
		textArea.setForeground(Color.WHITE);
		textArea.setBackground(Color.BLACK);
		contentPane.add(textArea, "cell 1 3,grow");

		JLabel lblNewLabel_2 = new JLabel("Szyfrogram");
		lblNewLabel_2.setFont(new Font("Verdana", Font.PLAIN, 12));
		lblNewLabel_2.setForeground(Color.WHITE);
		contentPane.add(lblNewLabel_2, "cell 0 4");

		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBackground(Color.BLACK);
		textArea_1.setFont(new Font("Verdana", Font.PLAIN, 12));
		contentPane.add(textArea_1, "cell 1 4,grow");

		JLabel lblWywietl = new JLabel("Wy\u015Bwietl: ");
		lblWywietl.setFont(new Font("Verdana", Font.PLAIN, 12));
		lblWywietl.setForeground(Color.WHITE);
		contentPane.add(lblWywietl, "cell 0 5,growx");

		JRadioButton rdbtnTekst = new JRadioButton("tekst");
		rdbtnTekst.setFont(new Font("Verdana", Font.PLAIN, 12));
		rdbtnTekst.setSelected(true);
		rdbtnTekst.setForeground(Color.WHITE);
		rdbtnTekst.setBackground(Color.DARK_GRAY);
		contentPane.add(rdbtnTekst, "flowx,cell 1 5,growx");

		JButton btnSzyfruj = new JButton("Szyfruj");
		btnSzyfruj.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				textArea_1.setText("Test");
			}
		});
		btnSzyfruj.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnSzyfruj.setForeground(Color.WHITE);
		btnSzyfruj.setBackground(Color.GRAY);
		contentPane.add(btnSzyfruj, "flowx,cell 1 6,growx,aligny center");

		JButton btnDeszyfruj = new JButton("Deszyfruj");
		btnDeszyfruj.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnDeszyfruj.setForeground(Color.WHITE);
		btnDeszyfruj.setBackground(Color.GRAY);
		contentPane.add(btnDeszyfruj, "cell 1 6,growx");

		JLabel lblNewLabel_1 = new JLabel("Informacje o wpisanym kluczu.");
		lblNewLabel_1.setFont(new Font("Verdana", Font.PLAIN, 12));
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel_1, "cell 1 1");

		JButton btnGenerujKlucz_1 = new JButton("Generuj klucz");
		btnGenerujKlucz_1.setFont(new Font("Verdana", Font.PLAIN, 12));
		btnGenerujKlucz_1.setForeground(Color.WHITE);
		btnGenerujKlucz_1.setBackground(Color.GRAY);
		btnGenerujKlucz_1.setToolTipText("Generuj klucz pseudolosowo");
		contentPane.add(btnGenerujKlucz_1, "cell 1 6,growx");

		JRadioButton rdbtnSzesnastkowo = new JRadioButton("szesnastkowo");
		rdbtnSzesnastkowo.setFont(new Font("Verdana", Font.PLAIN, 12));
		rdbtnSzesnastkowo.setForeground(Color.WHITE);
		rdbtnSzesnastkowo.setBackground(Color.DARK_GRAY);
		contentPane.add(rdbtnSzesnastkowo, "cell 1 5,growx");

		JRadioButton rdbtnBinarnie = new JRadioButton("binarnie");
		rdbtnBinarnie.setFont(new Font("Verdana", Font.PLAIN, 12));
		rdbtnBinarnie.setForeground(Color.WHITE);
		rdbtnBinarnie.setBackground(Color.DARK_GRAY);
		contentPane.add(rdbtnBinarnie, "cell 1 5,growx");
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Cryptography3DES frame = new Cryptography3DES();

					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

}
