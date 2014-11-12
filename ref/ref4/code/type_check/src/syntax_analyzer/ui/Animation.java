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

import java.util.*;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.*;

/**
 * @author hudsonr
 * Created on Apr 28, 2003
 */
public class Animation {

static final long DURATION = 210;

static long current;
static double progress;
static long start = -1;
static long finish;
static Viewport viewport;
static IFigure trackMe;
static IFigure showMe;
static Point trackLocation;

static boolean PLAYBACK;
static boolean RECORDING;

static Map initialStates;
static Map finalStates;

static void end() {
	Iterator iter = initialStates.keySet().iterator();
	while (iter.hasNext())
		((IFigure)iter.next()).revalidate();
	initialStates = null;
	finalStates = null;
	PLAYBACK = false;
	trackMe = null;
	showMe = null;
	viewport = null;
}

static void mark(IFigure figure) {
	trackMe = figure;
	trackLocation = trackMe.getBounds().getLocation();
	while (!(figure instanceof Viewport))
		figure = figure.getParent();
	viewport = (Viewport)figure;

	initialStates = new HashMap();
	finalStates = new HashMap();
	start = System.currentTimeMillis();
	finish = start + DURATION;
	current = start + 20;
}

static void captureLayout(IFigure root) {
	RECORDING = true;
	while (root.getParent()!= null)
		root = root.getParent();

	root.validate();
	Iterator iter = initialStates.keySet().iterator();
	while (iter.hasNext())
		recordFinalStates((IFigure)iter.next());
	RECORDING = false;
	PLAYBACK = true;
}

static boolean playbackState(IFigure container) {
	if (!PLAYBACK)
		return false;
	List initial = (List)initialStates.get(container);
	if (initial == null) {
		System.out.println("Error playing back state");
		return false;
	}
	List target = (List)finalStates.get(container);
	List children = container.getChildren();
	Rectangle rect1, rect2;
	for (int i = 0; i < children.size(); i++) {
		IFigure child = (IFigure)children.get(i);
		rect1 = (Rectangle)initial.get(i);
		rect2 = (Rectangle)target.get(i);
		child.setBounds(new Rectangle(
			(int)Math.round(progress * rect2.x + (1-progress) * rect1.x),
			(int)Math.round(progress * rect2.y + (1-progress) * rect1.y),
			(int)Math.round(progress * rect2.width + (1-progress) * rect1.width),
			(int)Math.round(progress * rect2.height + (1-progress) * rect1.height)
		));
//		child.invalidate();
	}
	return true;
}

static void recordFinalStates(IFigure container) {
	List list = new ArrayList();
	finalStates.put(container, list);
	List children = container.getChildren();
	list.clear();
	for (int i=0; i<children.size();i++)
		list.add(((IFigure)children.get(i)).getBounds().getCopy());
}

static void recordInitialState(IFigure container) {
	if (!RECORDING)
		return;
	List list = (List)initialStates.get(container);
	if (list != null)
		return;
//		System.out.println("Error recording initial state");
	initialStates.put(container, list = new ArrayList());
	List children = container.getChildren();
	list.clear();
	for (int i=0; i<children.size();i++)
		list.add(((IFigure)children.get(i)).getBounds().getCopy());
}

static void swap() {
	Map temp = finalStates;
	finalStates = initialStates;
	initialStates = temp;
}

static boolean step() {
	current = System.currentTimeMillis() + 30;
	progress = (double)(current - start)/(finish - start);
	progress = Math.min(progress, 0.999);
	Iterator iter = initialStates.keySet().iterator();
	
	while (iter.hasNext())
		((IFigure)iter.next()).revalidate();
	viewport.validate();
	
	Point loc = viewport.getViewLocation();
	loc.translate(trackMe.getBounds().getLocation().getDifference(trackLocation));
	viewport.setViewLocation(loc);
	trackLocation = trackMe.getBounds().getLocation();
	
	return current < finish;
}

}
