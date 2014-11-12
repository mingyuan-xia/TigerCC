package GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import Parse.Lexer;
import Parse.Token;
import Parse.Yylex;
import Parse.Grm;
import Parse.sym;

public class tigerFrame extends JFrame implements messPrinter {

	private static final long serialVersionUID = 1L;

	private JTree tree;

	public static void main(String argv[]) throws java.io.IOException {
		try {
			tigerFrame frame = new tigerFrame();
			frame.setVisible(true);

			if (argv.length > 0 && !argv[0].equals("")) {
				File f = new File(argv[0]);
				frame.setFile(f);

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "an error in main£º"
					+ e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
		}

	}

	final JSplitPane splitPane = new JSplitPane();

	final JSplitPane splitPane_1 = new JSplitPane();

	private JTextPane textArea;

	private boolean changed = false;

	private String text = "";

	private String filename = "";

	private String filepath = "";

	private JFrame self;

	private JList errorlist;

	/**
	 * Create the frame
	 */
	public tigerFrame() {
		super();
		if (!Config.initialed)
			Config.readConfig();
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "project initial error£º"
					+ e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
		}
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				if (changed) {
					if (JOptionPane.YES_OPTION == JOptionPane
							.showConfirmDialog(null,
									"The file is chaned, save it ?",
									"choose one", JOptionPane.YES_NO_OPTION)) {
						saveFile();
					}
				}
			}
		});
		self = this;
		setBounds(100, 100, 729, 602);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		self.setTitle("source file-...");

		// read the configs from xml file
		/** ********************************* */

		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		final JMenu fileMenu = new JMenu();
		fileMenu.setFont(new Font("Courier New", Font.BOLD, 12));
		fileMenu.setText("file(F)");
		fileMenu.setMnemonic('F');
		menuBar.add(fileMenu);

		final JMenuItem fileMenuItem = new JMenuItem();
		fileMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (changed) {
					if (JOptionPane.YES_OPTION == JOptionPane
							.showConfirmDialog(null,
									"The file is chaned, save it ?",
									"choose one", JOptionPane.YES_NO_OPTION)) {
						saveFile();

					}
				}
				File file = new File(GUI.Constants.folder);
				JFileChooser chooser = new JFileChooser(file);
				chooser.setDialogTitle("choose source file");
				chooser.setFileFilter(new tigFileFilter());
				if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(null)) {
					File f = chooser.getSelectedFile();
					if (f.isFile()) {
						filename = "";
						text = "";
						changed = false;
						filepath = f.getAbsolutePath();
						filename = f.getName();
						self.setTitle("source file-" + f.getAbsolutePath());

						openFile();
					}
				}
			}
		});
		fileMenuItem.setFont(new Font("Courier New", Font.BOLD, 12));
		fileMenuItem.setText("open");
		fileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				KeyEvent.CTRL_MASK));
		fileMenu.add(fileMenuItem);

		final JMenuItem saveMenuItem = new JMenuItem();
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (changed)
					saveFile();
			}
		});
		saveMenuItem.setFont(new Font("Courier New", Font.BOLD, 12));
		saveMenuItem.setText("save");
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				KeyEvent.CTRL_MASK));
		fileMenu.add(saveMenuItem);

		final JMenu toolsMenu = new JMenu();
		toolsMenu.setFont(new Font("Courier New", Font.BOLD, 12));
		toolsMenu.setText("tools(T)");
		toolsMenu.setMnemonic('T');
		menuBar.add(toolsMenu);

		final JMenuItem runMenuItem = new JMenuItem();
		runMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (changed) {
					if (JOptionPane.YES_OPTION == JOptionPane
							.showConfirmDialog(null,
									"The file is chaned, save it ?",
									"choose one", JOptionPane.YES_NO_OPTION)) {
						saveFile();
					} else
						return;

				}
				// parseFile();
				build();

			}
		});
		runMenuItem.setFont(new Font("Courier New", Font.BOLD, 12));
		runMenuItem.setText("build");

		toolsMenu.add(runMenuItem);
		runMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
				KeyEvent.CTRL_MASK));

		splitPane.setDividerSize(2);
		splitPane.setDividerLocation(150);
		getContentPane().add(splitPane, BorderLayout.CENTER);

		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_1.setDividerSize(2);
		splitPane_1.setDividerLocation(450);
		splitPane.setRightComponent(splitPane_1);

		final JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout());
		splitPane_1.setLeftComponent(panel_1);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_1.add(scrollPane, BorderLayout.CENTER);

		textArea = new JTextPane();

		textArea.setFont(new Font("Courier New", Font.PLAIN, 14));
		textArea.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent arg0) {
				int pos = textArea.getCaretPosition();
				if (!arg0.isAltDown() && !arg0.isControlDown())
					changed = true;
				colorText();
				textArea.setCaretPosition(pos);
			}
		});

		scrollPane.setViewportView(textArea);

		final JPanel panel_2 = new JPanel();
		panel_2.setLayout(new BorderLayout());
		splitPane_1.setRightComponent(panel_2);

		final JScrollPane scrollPane_1 = new JScrollPane();
		panel_2.add(scrollPane_1, BorderLayout.CENTER);

		errorlist = new JList();

		errorlist.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				int index = errorlist.getSelectedIndex();
				errorMouseCilcked(index);
			}
		});
		errorlist.setCellRenderer(new ErrorListCellRenderer());
		errorlist.setFont(new Font("Courier New", Font.PLAIN, 13));
		scrollPane_1.setViewportView(errorlist);

		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		splitPane.setLeftComponent(panel);

		final JScrollPane scrollPane_2 = new JScrollPane();
		panel.add(scrollPane_2, BorderLayout.CENTER);

		tree = new JTree();
		tree.setFont(new Font("Courier New", Font.PLAIN, 13));
		scrollPane_2.setViewportView(tree);
		tree.setModel(new DefaultTreeModel(null));
	}

	public void setFile(File f) {
		filename = f.getName();
		filepath = f.getAbsolutePath();
		self.setTitle("Source file-" + f.getAbsolutePath());
		openFile();
	}

	protected void openFile() {
		File f = new File(filepath);

		FileReader fr;
		try {
			fr = new FileReader(f);

			BufferedReader d = new BufferedReader(fr);

			while (true) {
				String s = d.readLine();
				if (s == null)
					break;
				text += s + "\n";
			}

			textArea.setText(text);
			colorText();
		} catch (FileNotFoundException e) {

			JOptionPane.showMessageDialog(null, "Open file error £º"
					+ e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
			// e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Read file error £º"
					+ e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
			// e.printStackTrace();
		}
	}

	protected String transfer(String reg, String replace, String text) {
		String tmp = text;
		String re = "";
		int index = -1;
		while ((index = tmp.indexOf(reg)) >= 0) {
			re += tmp.substring(0, index);
			re += replace;
			tmp = tmp.substring(index + reg.length(), tmp.length());
		}
		re += tmp;
		return re;
	}

	protected void saveFile() {
		if (filename == "" || filepath == "") {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle("save file");
			chooser.setFileFilter(new tigFileFilter());
			int re = chooser.showSaveDialog(null);
			if (re == JFileChooser.ERROR_OPTION)
				JOptionPane.showMessageDialog(null, "Save file error £º",
						"error", JOptionPane.ERROR_MESSAGE);
			else if (re == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				filepath = file.getAbsolutePath();
				filename = file.getName();
				if (filename.equals("")) {
					filepath = "tig";
					filename = "tig";
				}
				if (!filename.endsWith(".tig") || !filename.endsWith(".java")) {
					filepath += ".tig";
					filename += ".tig";
				}

			}
		}

		text = textArea.getText();
		text = text.replaceAll("\r\n", "\n");
		File f = new File(filepath);
		try {
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(text.getBytes());
			fo.close();
			changed = false;
			colorText();

		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Find file error£º"
					+ e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
			// e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Access file error :"
					+ e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
			// e.printStackTrace();
		}
	}

	static SimpleAttributeSet COMM = new SimpleAttributeSet();

	static SimpleAttributeSet KEY = new SimpleAttributeSet();

	static SimpleAttributeSet BLACK = new SimpleAttributeSet();

	static SimpleAttributeSet STRING = new SimpleAttributeSet();
	// Best to reuse attribute sets as much as possible.
	static {
		if (!Config.initialed)
			Config.readConfig();
		StyleConstants.setForeground(COMM, GUI.Constants.comm_color);
		StyleConstants.setFontFamily(COMM, GUI.Constants.font);
		StyleConstants.setFontSize(COMM, GUI.Constants.fontsize);

		StyleConstants.setForeground(KEY, GUI.Constants.key_color);
		StyleConstants.setBold(KEY, true);
		StyleConstants.setFontFamily(KEY, GUI.Constants.font);
		StyleConstants.setFontSize(KEY, GUI.Constants.fontsize);

		StyleConstants.setForeground(BLACK, GUI.Constants.normal_color);
		StyleConstants.setFontFamily(BLACK, GUI.Constants.font);
		StyleConstants.setFontSize(BLACK, GUI.Constants.fontsize);

		StyleConstants.setForeground(STRING, GUI.Constants.string_color);
		StyleConstants.setItalic(STRING, true);
		StyleConstants.setFontFamily(STRING, GUI.Constants.font);
		StyleConstants.setFontSize(STRING, GUI.Constants.fontsize);
	}

	protected void insertText(String text, AttributeSet set) {
		try {
			String re = text.replaceAll("\r\n", "\n");
			textArea.getDocument().insertString(
					textArea.getDocument().getLength(), re, set);
		} catch (BadLocationException e) {
			JOptionPane.showMessageDialog(null, "text display error :"
					+ e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void colorText() {
		ErrorMsg.ErrorMsg errorMsg = new ErrorMsg.ErrorMsg(filename, null);
		try {

			int begin = 0;
			String temp = "";
			text = textArea.getText();
			textArea.setText("");

			Lexer m_lexer = new Yylex(new StringReader(text), errorMsg);
			Parse.Token tok;

			do {
				tok = (Token) m_lexer.nextToken();

				if (GUI.Config.keys.containsKey(new Integer(tok.sym))) {
					if (tok.sym == sym.ID) {
						String lkey = text.substring(tok.left, tok.right);
						if (GUI.Config.ids.containsKey(lkey)) {
							temp = text.substring(begin, tok.left);
							insertText(temp, BLACK);

							insertText(lkey, KEY);
							begin = tok.right;
						}
					} else if (tok.sym == sym.STRING) {
						temp = text.substring(begin, tok.left);
						insertText(temp, BLACK);
						insertText(text.substring(tok.left, tok.right), STRING);
						begin = tok.right;
					} else if (tok.sym == sym.COMM) {

						temp = text.substring(begin, tok.left);
						insertText(temp, BLACK);

						temp = text.substring(tok.left, tok.right);
						insertText(temp, COMM);
						begin = tok.right;
					} else {
						String lkey = text.substring(tok.left, tok.right);
						temp = text.substring(begin, tok.left);
						insertText(temp, BLACK);

						insertText(lkey, KEY);

						begin = tok.left + lkey.length();
					}
				}
			} while (tok.sym != sym.EOF);

			if (tok.getValue() != null && begin < text.length()) {
				int left, i;
				String s = (String) tok.getValue();
				if ((i = s.indexOf('_')) != -1) {
					left = Integer.parseInt(s.substring(0, i));
					if (s.substring(i + 1, s.length()).equals("STRING")) {
						temp = text.substring(begin, left);
						insertText(temp, BLACK);
						insertText(text.substring(left, tok.right), STRING);
						begin = tok.right;
					} else if (s.substring(i + 1, s.length()).equals("COMM")) {
						temp = text.substring(begin, left);
						insertText(temp, BLACK);

						temp = text.substring(left, tok.right);
						insertText(temp, COMM);
						begin = tok.right;
					}
				}
			}
			if (begin < text.length()) {
				temp = text.substring(begin, text.length());
				insertText(temp, BLACK);
			}

		} catch (FileNotFoundException e) {

			JOptionPane.showMessageDialog(null, "Open file error £º"
					+ e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
			// e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Read file error £º"
					+ e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
			// e.printStackTrace();
		}
	}

	protected void build() {
		ErrorMsg.ErrorMsg errorMsg = new ErrorMsg.ErrorMsg(filename, this);
		java.io.InputStream inp = null;

		// /////////////////////////
		this.clearErrList();
		// ///////////////////////////////////
		try {

			inp = new java.io.FileInputStream(filepath);

			Grm parser = new Grm(new Yylex(inp, errorMsg), errorMsg);

			File file = new File(filepath);

			file = new File(file.getParent() + File.separator
					+ file.getName().substring(0, file.getName().length() - 4)
					+ ".abs");
			// System.out.println(file.getAbsolutePath());
			java.io.FileOutputStream ost = new java.io.FileOutputStream(file);
			// file=new File();
			Absyn.Print printer = new Absyn.Print(new java.io.PrintStream(ost));
			Absyn.Exp value = (Absyn.Exp) (parser./* debug_ */parse().value);
			printer.prExp(value, 1);
			Semant.Semant s = new Semant.Semant(new ErrorMsg.ErrorMsg(filename,
					this), textArea.getText());
			s.transExp(value);

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) parser
					.getRe().getRoot();
			DefaultTreeModel model = new DefaultTreeModel(node);
			tree.setModel(model);

			ost.close();
		} catch (Exception e) {
			// Auto-generated catch block
			System.out.println("find an error in tiger resource file!");
			tree.setModel(null);
			// e.printStackTrace();
		} finally {
			try {
				inp.close();
				// ////////////////////////////////////////////////
				addTail();
				// /////////////////////////////////
			} catch (java.io.IOException e) {
			}
		}

	}

	protected void errorMouseCilcked(int index) {
		if (index >= 0 && index < errorlist.getModel().getSize()) {

			DefaultListModel def = (DefaultListModel) errorlist.getModel();
			Error e = (Error) def.get(index);
			if (e.getLine() >= 0) {
				text = textArea.getText();
				int begin = 0, end = 0;
				int count = 0, i;
				for (i = 0; i < text.length(); i++) {
					if (text.charAt(i) == '\n') {
						count++;
						if (count == e.getLine() - 1)
							begin = i + 1;

						if (count == e.getLine()) {
							end = i;
							break;
						}
					}
				}

				textArea.setSelectionStart(begin);
				textArea.setSelectionEnd(end);
				textArea.requestFocus();
			}
		}
	}

	public int errNum = 0;

	public void clearErrList() {

		errNum = 0;
		if (errorlist.getModel().getSize() > 0) {
			errorlist.removeAll();
			errorlist.setModel(new DefaultListModel());
			errorlist.revalidate();
			errorlist.repaint();
		}
	}

	public void clearModel() {
		if (errorlist.getModel().getSize() > 0) {
			errorlist.removeAll();
			errorlist.setModel(new DefaultListModel());
			errorlist.revalidate();
			errorlist.repaint();
		}
	}

	public void addTail() {
		DefaultListModel def = new DefaultListModel();
		for (int i = 0; i < errorlist.getModel().getSize(); i++)
			def.addElement(errorlist.getModel().getElementAt(i));

		if (errNum > 0) {
			Error e = new Error(-1,
					"error  end ***********************************");
			def.addElement(e);
		}
		Error e = new Error(-1, errNum + " errors");
		def.addElement(e);

		clearModel();
		errorlist.setModel(def);
	}

	public void printErr(String s) {
		// Auto-generated method stub
		DefaultListModel def = new DefaultListModel();
		for (int i = 0; i < errorlist.getModel().getSize(); i++)
			def.addElement(errorlist.getModel().getElementAt(i));

		errNum++;
		Error e = new Error(-2, "Error " + errNum + ": " + s);
		def.addElement(e);

		clearModel();
		errorlist.setModel(def);
	}

	public void printErr(int s) {
		// Auto-generated method stub
		printErr("" + s);
	}

	public void printMes(String s) {
		// Auto-generated method stub
		DefaultListModel def = new DefaultListModel();
		for (int i = 0; i < errorlist.getModel().getSize(); i++)
			def.addElement(errorlist.getModel().getElementAt(i));

		Error e = new Error(-1, "Message: " + s);
		def.addElement(e);

		clearModel();
		errorlist.setModel(def);
	}

	public void printMes(int s) {
		// Auto-generated method stub
		printErr("" + s);
	}

	public void printErr(Error s) {
		// Auto-generated method stub
		DefaultListModel def = new DefaultListModel();
		for (int i = 0; i < errorlist.getModel().getSize(); i++)
			def.addElement(errorlist.getModel().getElementAt(i));

		errNum++;
		Error n = s;
		n.setErr(errNum + ": Line: " + s.getLine() + " " + s.getErr());
		def.addElement(n);

		clearModel();
		errorlist.setModel(def);
	}
}

class ErrorListCellRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = 1L;

	final static ImageIcon errorIcon = new ImageIcon(GUI.Constants.error_img);

	final static ImageIcon warnigIcon = new ImageIcon(GUI.Constants.warning_img);

	// This is the only method defined by ListCellRenderer.
	// We just reconfigure the JLabel each time we're called.
	ErrorListCellRenderer() {
		this.setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value, // value
			int index, // cell index
			boolean isSelected, // is the cell selected
			boolean cellHasFocus) // the list and the cell have the focus
	{
		if (value != null) {
			setText(((Error) value).getErr());
			if (index >= 0 && index < list.getModel().getSize()) {
				{
					Error e = (Error) list.getModel().getElementAt(index);
					if (e.getLine() != -1)
						setIcon(errorIcon);
					else
						setIcon(null);
				}

			}
		}
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		return this;
	}

}

class tigFileFilter extends javax.swing.filechooser.FileFilter {
	String ex = "";

	public boolean accept(File arg0) {
		// Auto-generated method stub
		if (arg0.isDirectory())
			return true;

		String name = arg0.getName();
		int index = name.indexOf(".");
		if (index > 0 && index < name.length() - 1) {
			ex = name.substring(index + 1, name.length());
			if (ex.equals("tig") || ex.equals("java"))
				return true;
		}
		return false;
	}

	public String getDescription() {
		return "text file(*.tig;*.java)";

	}
}