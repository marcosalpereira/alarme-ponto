package org.marcosoft.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

public class SwingUtil {
	
	/**
	 * Centralizar a janela na tela.
	 * @param window janela
	 */
	public static void center(Window window) {
	    Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
	    int screenWidth = screenSize.width;
	    int screenHeight = screenSize.height;
	
	    window.setLocation(screenWidth / 2 - window.getWidth() / 2, screenHeight / 2 - window.getHeight() / 2);
	}
	
}

