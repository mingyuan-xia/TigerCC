/**
 * This class is responsible for the plus and minus operation of nodes in the canvas.
 * Changes made from the original file.
 * 
 * @author Jessie
 * @since 5/10/2008
 */

/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package syntax_analyzer.ui;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Toggle;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * 
 * @author hudsonr
 * Created on Apr 22, 2003
 */
public class PlusMinus extends Toggle {

{setPreferredSize(9, 9);}

/**
 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
 */
protected void paintFigure(Graphics g) {
	super.paintFigure(g);
	Rectangle r = Rectangle.SINGLETON;
	r.setBounds(getBounds()).resize(-1, -1);
	g.drawRectangle(r);
	int xMid = r.x + r.width / 2;
	int yMid = r.y + r.height / 2;
	g.drawLine(r.x + 2, yMid, r.right() - 2, yMid);
	if (!isSelected())
		g.drawLine(xMid, r.y + 2, xMid, r.bottom() - 2);
}

}
