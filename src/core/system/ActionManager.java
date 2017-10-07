package core.system;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultEditorKit;

import core.IIDE;

public class ActionManager {
	private static IIDE jideInstance;
	
	public static void init(IIDE jideInst) {
		jideInstance = jideInst;
	}
	
	public static Action Run = new AbstractAction("Run", new ImageIcon(IIDE.class.getResource("images/run.jpg"))) {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			if (IIDE.currentFile.equals("Untitled")) {
				FileManager.saveFileAs();
			}
			if (!IIDE.currentFile.equals("Untitled")) {
				FileManager.saveFile(IIDE.currentFile);
				jideInstance.setTitle(IIDE.currentFile);
				Terminate.setEnabled(true);
				IIDE.console.setText("");
				ConsoleManager.addedText = "";
				IIDE.console.setEditable(true);
				ConsoleManager.buildAndRun(IIDE.currentFile);
				IIDE.console.setEditable(false);
			}
		}
	};

	public static Action Terminate = new AbstractAction("Terminate", new ImageIcon(IIDE.class.getResource("images/terminate.jpg"))) {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			if (ConsoleManager.pr == null)
				return;
			if (ConsoleManager.pr.isAlive())
				ConsoleManager.pr.destroy();

			if (ConsoleManager.pr.isAlive())
				ConsoleManager.pr.destroyForcibly();

			IIDE.console.setEditable(false);
			IIDE.console.setText("");
			ConsoleManager.addedText = "";
		}
	};

	public static Action New = new AbstractAction("New", new ImageIcon(IIDE.class.getResource("images/new.gif"))) {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			if (JOptionPane.showConfirmDialog(jideInstance.getRootPane(), "Would you like to save " + IIDE.currentFile + "?", "Save",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				FileManager.saveFile(IIDE.currentFile);
			}

			IIDE.editor.setText("");
			IIDE.currentFile = "Untitled";
			jideInstance.setTitle(IIDE.currentFile);
			jideInstance.changed = false;
			Save.setEnabled(false);
			SaveAs.setEnabled(false);
		}
	};

	public static Action Open = new AbstractAction("Open", new ImageIcon(IIDE.class.getResource("images/open.gif"))) {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			FileManager.saveOld();
			if (IIDE.dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				FileManager.readInFile(IIDE.dialog.getSelectedFile().getAbsolutePath());
			}

			SaveAs.setEnabled(true);
			
			jideInstance.updateSyntaxHighlighting();
		}
	};

	public static Action Save = new AbstractAction("Save", new ImageIcon(IIDE.class.getResource("images/save.gif"))) {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			if (!IIDE.currentFile.equals("Untitled")) {
				FileManager.saveFile(IIDE.currentFile);
			} else {
				FileManager.saveFileAs();
			}

			jideInstance.setTitle(IIDE.currentFile);
		}
	};

	public static Action SaveAs = new AbstractAction("Save as...") {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			FileManager.saveFileAs();
		}
	};

	public static Action Quit = new AbstractAction("Quit") {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			FileManager.saveOld();
			System.exit(0);
		}
	};

	private static ActionMap m = IIDE.editor.getActionMap();
	public static Action Cut = m.get(DefaultEditorKit.cutAction);
	public static Action Copy = m.get(DefaultEditorKit.copyAction);
	public static Action Paste = m.get(DefaultEditorKit.pasteAction);
}
