package core.system;

import java.awt.Toolkit;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import core.IIDE;

public class FileManager {
	static IIDE jideInstance;
	
	public static void init(IIDE jideInst) {
		jideInstance = jideInst;
	}
	
	public static void saveFileAs() {
		if (IIDE.dialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			saveFile(IIDE.dialog.getSelectedFile().getAbsolutePath());
		}
	}

	public static void saveOld() {
		if (jideInstance.changed) {
			if (JOptionPane.showConfirmDialog(jideInstance, "Would you like to save " + IIDE.currentFile + "?", "Save",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				saveFile(IIDE.currentFile);
			}
		}
	}

	public static void readInFile(String fileName) {
		try {
			FileReader r = new FileReader(fileName);
			IIDE.editor.read(r, null);
			r.close();
			jideInstance.getContentPane();
			IIDE.currentFile = fileName;
			jideInstance.setTitle(IIDE.currentFile);
			jideInstance.changed = false;
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(jideInstance, "Editor can't find the file: " + fileName);
		}
	}

	public static void saveFile(String fileName) {
		try {
			FileWriter w = new FileWriter(fileName);
			IIDE.editor.write(w);
			w.close();
			IIDE.currentFile = fileName;

			ActionManager.Save.setEnabled(false);
			jideInstance.changed = false;
			jideInstance.saved = true;
		} catch (IOException e) {
		}
	}
}
