// Copyright 2010 DEF
//
// This file is part of V3dScene.
//
// V3dScene is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// V3dScene is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with V3dScene.  If not, see <http://www.gnu.org/licenses/>.

package fr.def.iss.vd2.lib_v3d.gui;

import org.fenggui.Label;
import org.fenggui.decorator.background.PlainBackground;
import org.fenggui.text.content.factory.simple.TextStyle;
import org.fenggui.text.content.factory.simple.TextStyleEntry;
import org.fenggui.util.Color;
import org.fenggui.util.Dimension;
import org.fenggui.util.Point;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;

/**
 *
 * @author fberto
 */
public class V3DLabel extends V3DGuiComponent {

    Label label;
    private int xPos;
    private int yPos;
    

    public V3DLabel(V3DContext context, String text) {
        label = new Label(text);
        label.setXY(0, 0);
        label.setExpandable(false);
        label.getAppearance().add(new PlainBackground(new Color(1.0f,1.0f,1.0f,0.8f)));
        label.setShrinkable(true);
        
    }

    public void setColor(V3DColor foregroundColor, V3DColor backgroundColor) {
        label.getAppearance().removeAll();
        label.getAppearance().add(new PlainBackground(new Color(backgroundColor.r,backgroundColor.g,backgroundColor.b,backgroundColor.a)));

        TextStyle style = label.getAppearance().getStyle(TextStyle.DEFAULTSTYLEKEY);
        TextStyleEntry  textEntryStyle = new TextStyleEntry();
        textEntryStyle.setColor(new Color(foregroundColor.r,foregroundColor.g,foregroundColor.b,foregroundColor.a));
        style.addStyle(TextStyleEntry.DEFAULTSTYLESTATEKEY, textEntryStyle);

    }

    @Override
    public Label getFenGUIWidget() {
        return label;
    }

    @Override
    public boolean containsPoint(int x, int y) {
         if(x < this.x || y < this.y) {
            return false;
        }

        if(x > this.x + label.getWidth() || y > this.y + label.getHeight()) {
            return false;
        }

        return true;
    }

    @Override
    void repack() {
        
        if(parent != null) {
            xPos = 0;
            yPos = 0;
            if(xAlignment == GuiXAlignment.LEFT) {
                xPos = x;
            } else {
                xPos = parent.getWidth() - label.getWidth() - x;
            }

            if(yAlignment == GuiYAlignment.BOTTOM) {
                yPos = y;
            } else {
                yPos = parent.getHeight() - label.getHeight() - y;
            }

            label.setXY(xPos,  yPos);
        }
        label.updateMinSize();
        label.setSizeToMinSize();
        label.layout();
    }

    public void setText(String text) {
        label.setText(text);
        if(parent != null) {
            parent.repack();
        }
    }

    public Point getSize() {
        Dimension dim = label.getSize();

        return new Point(dim.getWidth(), dim.getHeight());
    }

    public Point getComputedPosition() {
        Point position = label.getPosition();
        if(parent != null) {
            return new Point(xPos, yPos);
        }else {
            return position;
        }
        
    }

}
