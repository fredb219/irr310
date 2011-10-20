/*
 * FengGUI - Java GUIs in OpenGL (http://www.fenggui.org)
 * 
 * Copyright (c) 2005-2009 FengGUI Project
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details:
 * http://www.gnu.org/copyleft/lesser.html#TOC3
 * 
 * Created on 21.03.2008
 * $Id$
 */
package org.fenggui.event;

/**
 * 
 * @author marcmenghin, last edited by $Author$, $Date$
 * @version $Revision$
 */
public interface IElementListener<T>
{
  public abstract void enteredElement(ElementEvent<T> event);

  public abstract void leftElement(ElementEvent<T> event);

  public abstract void activatedElement(ElementEvent<T> event);
}
