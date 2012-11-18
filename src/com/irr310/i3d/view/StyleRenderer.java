package com.irr310.i3d.view;

import org.lwjgl.opengl.GL11;

import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.view.BorderParams.CornerStyle;

public class StyleRenderer {

    
    
    
    private final View view;
    private LayoutParams layoutParams;
    private BorderParams borderParams;
    private float height;
    private float width;
    private float left;
    private float top;
    private float right;
    private float bottom;
    private Color debugColor;
    private Graphics g;
    private float cornerLeftTopSize;
    private float cornerLeftBottomSize;
    private float cornerRightTopSize;
    private float cornerRightBottomSize;

    public StyleRenderer(View view) {
        this.view = view;
        debugColor = Color.randomLightOpaqueColor();
        
    }

    public void draw(Graphics g) {
            this.g = g;
            initDraw();

            //drawDebugBox();
            drawBackground(g);
            drawBorder(g);
    }

    private void drawBorder(Graphics g) {
        float borderSize = layoutParams.computeMesure(borderParams.getBorderSize());
        if(borderSize > 0) {
            g.setColor(borderParams.getBorderColor());
            
            GL11.glBegin(GL11.GL_QUADS);
            
            // all except corners
            GL11.glVertex2d(left - borderSize, top + cornerLeftTopSize);
            GL11.glVertex2d(left + borderSize, top + cornerLeftTopSize);
            GL11.glVertex2d(left + borderSize, bottom - cornerLeftBottomSize);
            GL11.glVertex2d(left - borderSize, bottom - cornerLeftBottomSize);
            
            GL11.glVertex2d(right - borderSize, top + cornerRightTopSize);
            GL11.glVertex2d(right + borderSize, top + cornerRightTopSize);
            GL11.glVertex2d(right + borderSize, bottom - cornerRightBottomSize);
            GL11.glVertex2d(right - borderSize, bottom - cornerRightBottomSize);
            
            
            GL11.glVertex2d(left + cornerLeftTopSize, top + borderSize);
            GL11.glVertex2d(left + cornerLeftTopSize, top - borderSize);
            GL11.glVertex2d(right - cornerRightTopSize, top - borderSize);
            GL11.glVertex2d(right - cornerRightTopSize, top + borderSize);
            
            GL11.glVertex2d(left + cornerLeftBottomSize, bottom + borderSize);
            GL11.glVertex2d(left + cornerLeftBottomSize, bottom - borderSize);
            GL11.glVertex2d(right - cornerRightBottomSize, bottom - borderSize);
            GL11.glVertex2d(right - cornerRightBottomSize, bottom + borderSize);
            
            if (borderParams.getCornerLeftTopStyle() == CornerStyle.SQUARE) {
                GL11.glVertex2d(left - borderSize, top - borderSize);
                GL11.glVertex2d(left + cornerLeftTopSize, top - borderSize);
                GL11.glVertex2d(left + cornerLeftTopSize, top + borderSize);
                GL11.glVertex2d(left + borderSize, top + borderSize);
                
                GL11.glVertex2d(left - borderSize, top - borderSize);
                GL11.glVertex2d(left - borderSize, top + cornerLeftTopSize);
                GL11.glVertex2d(left + borderSize, top + cornerLeftTopSize);
                GL11.glVertex2d(left + borderSize, top + borderSize);
            } else if(borderParams.getCornerLeftTopStyle() == CornerStyle.BEVEL) {
                
            }
            
            if (borderParams.getCornerLeftBottomStyle() == CornerStyle.SQUARE) {
                GL11.glVertex2d(left - borderSize, bottom + borderSize);
                GL11.glVertex2d(left + cornerLeftBottomSize, bottom + borderSize);
                GL11.glVertex2d(left + cornerLeftBottomSize, bottom - borderSize);
                GL11.glVertex2d(left + borderSize, bottom - borderSize);
                
                GL11.glVertex2d(left - borderSize, bottom + borderSize);
                GL11.glVertex2d(left - borderSize, bottom - cornerLeftTopSize);
                GL11.glVertex2d(left + borderSize, bottom - cornerLeftTopSize);
                GL11.glVertex2d(left + borderSize, bottom - borderSize);
            }
            
            if (borderParams.getCornerRightBottomStyle() == CornerStyle.SQUARE) {
                GL11.glVertex2d(right + borderSize, bottom + borderSize);
                GL11.glVertex2d(right - cornerRightBottomSize, bottom + borderSize);
                GL11.glVertex2d(right - cornerRightBottomSize, bottom - borderSize);
                GL11.glVertex2d(right - borderSize, bottom - borderSize);
                
                GL11.glVertex2d(right + borderSize, bottom + borderSize);
                GL11.glVertex2d(right + borderSize, bottom - cornerLeftTopSize);
                GL11.glVertex2d(right - borderSize, bottom - cornerLeftTopSize);
                GL11.glVertex2d(right - borderSize, bottom - borderSize);
            }
            
            if (borderParams.getCornerRightTopStyle() == CornerStyle.SQUARE) {
                GL11.glVertex2d(right + borderSize, top - borderSize);
                GL11.glVertex2d(right - cornerRightTopSize, top - borderSize);
                GL11.glVertex2d(right - cornerRightTopSize, top + borderSize);
                GL11.glVertex2d(right - borderSize, top + borderSize);
                
                GL11.glVertex2d(right + borderSize, top - borderSize);
                GL11.glVertex2d(right + borderSize, top + cornerLeftTopSize);
                GL11.glVertex2d(right - borderSize, top + cornerLeftTopSize);
                GL11.glVertex2d(right - borderSize, top + borderSize);
            }
            
            GL11.glEnd();
        }
    }

    private void drawBackground(Graphics g) {
        if (borderParams.getBackground() != null) {

            Drawable background = borderParams.getBackground();
            background.setGraphics(g);
            background.setBounds(left, top, right, bottom);
            

            // Top left
            background.begin(GL11.GL_QUADS);

            if (borderParams.getCornerLeftTopStyle() == CornerStyle.SQUARE) {
                background.vertex(left, top);
                background.vertex(left + cornerLeftTopSize, top);
                background.vertex(left + cornerLeftTopSize, top + cornerLeftTopSize);
                background.vertex(left, top + cornerLeftTopSize);
            }

            background.vertex(left + cornerLeftTopSize, top);
            background.vertex(left + width / 2, top);
            background.vertex(left + width / 2, top + height / 2);
            background.vertex(left + cornerLeftTopSize, top + height / 2);

            background.vertex(left, top + cornerLeftTopSize);
            background.vertex(left + cornerLeftTopSize, top + cornerLeftTopSize);
            background.vertex(left + cornerLeftTopSize, top + height / 2);
            background.vertex(left, top + height / 2);

            background.end();

            if (borderParams.getCornerLeftTopStyle() == CornerStyle.BEVEL) {
                background.begin(GL11.GL_TRIANGLES);
                background.vertex(left + cornerLeftTopSize, top);
                background.vertex(left + cornerLeftTopSize, top + cornerLeftTopSize);
                background.vertex(left, top + cornerLeftTopSize);
                background.end();
            }

            // Top right

            background.begin(GL11.GL_QUADS);

            if (borderParams.getCornerRightTopStyle() == CornerStyle.SQUARE) {
                background.vertex(right, top);
                background.vertex(right - cornerRightTopSize, top);
                background.vertex(right - cornerRightTopSize, top + cornerRightTopSize);
                background.vertex(right, top + cornerRightTopSize);
            }

            background.vertex(right - cornerRightTopSize, top);
            background.vertex(right - width / 2, top);
            background.vertex(right - width / 2, top + height / 2);
            background.vertex(right - cornerRightTopSize, top + height / 2);

            background.vertex(right, top + cornerLeftTopSize);
            background.vertex(right - cornerRightTopSize, top + cornerRightTopSize);
            background.vertex(right - cornerRightTopSize, top + height / 2);
            background.vertex(right, top + height / 2);

            background.end();

            if (borderParams.getCornerRightTopStyle() == CornerStyle.BEVEL) {
                background.begin(GL11.GL_TRIANGLES);
                background.vertex(right - cornerRightTopSize, top);
                background.vertex(right - cornerRightTopSize, top + cornerRightTopSize);
                background.vertex(right, top + cornerRightTopSize);
                background.end();
            }

            // Bottom left
            background.begin(GL11.GL_QUADS);

            if (borderParams.getCornerLeftBottomStyle() == CornerStyle.SQUARE) {
                background.vertex(left, bottom);
                background.vertex(left + cornerLeftBottomSize, bottom);
                background.vertex(left + cornerLeftBottomSize, bottom - cornerLeftBottomSize);
                background.vertex(left, bottom - cornerLeftBottomSize);
            }

            background.vertex(left + cornerLeftBottomSize, bottom);
            background.vertex(left + width / 2, bottom);
            background.vertex(left + width / 2, bottom - height / 2);
            background.vertex(left + cornerLeftBottomSize, bottom - height / 2);

            background.vertex(left, bottom - cornerLeftBottomSize);
            background.vertex(left + cornerLeftBottomSize, bottom - cornerLeftBottomSize);
            background.vertex(left + cornerLeftBottomSize, bottom - height / 2);
            background.vertex(left, bottom - height / 2);

            background.end();

            if (borderParams.getCornerLeftBottomStyle() == CornerStyle.BEVEL) {
                background.begin(GL11.GL_TRIANGLES);
                background.vertex(left + cornerLeftBottomSize, bottom);
                background.vertex(left + cornerLeftBottomSize, bottom - cornerLeftBottomSize);
                background.vertex(left, bottom - cornerLeftBottomSize);
                background.end();
            }

            // Bottom right
            background.begin(GL11.GL_QUADS);

            if (borderParams.getCornerRightBottomStyle() == CornerStyle.SQUARE) {
                background.vertex(right, bottom);
                background.vertex(right - cornerRightBottomSize, bottom);
                background.vertex(right - cornerRightBottomSize, bottom - cornerRightBottomSize);
                background.vertex(right, bottom - cornerLeftBottomSize);
            }

            background.vertex(right - cornerRightBottomSize, bottom);
            background.vertex(right - width / 2, bottom);
            background.vertex(right - width / 2, bottom - height / 2);
            background.vertex(right - cornerRightBottomSize, bottom - height / 2);

            background.vertex(right, bottom - cornerRightBottomSize);
            background.vertex(right - cornerRightBottomSize, bottom - cornerRightBottomSize);
            background.vertex(right - cornerRightBottomSize, bottom - height / 2);
            background.vertex(right, bottom - height / 2);

            background.end();

            if (borderParams.getCornerRightBottomStyle() == CornerStyle.BEVEL) {
                background.begin(GL11.GL_TRIANGLES);
                background.vertex(right - cornerRightBottomSize, bottom);
                background.vertex(right - cornerRightBottomSize, bottom - cornerRightBottomSize);
                background.vertex(right, bottom - cornerRightBottomSize);
                background.end();
            }

            
            background.close();
        }
    }

    private void initDraw() {
        layoutParams = view.layoutParams;
        borderParams = view.borderParams;
        
        height = layoutParams.getHeight() + layoutParams.computeMesure(layoutParams.getLayoutPaddingTop())
                + layoutParams.computeMesure(layoutParams.getLayoutPaddingBottom());
        width = layoutParams.getWidth() + layoutParams.computeMesure(layoutParams.getLayoutPaddingLeft())
                + layoutParams.computeMesure(layoutParams.getLayoutPaddingRight());
        left = -layoutParams.computeMesure(layoutParams.getLayoutPaddingLeft());
        top = -layoutParams.computeMesure(layoutParams.getLayoutPaddingTop());
        right = left + width;
        bottom = top + height;
        
        cornerLeftTopSize = layoutParams.computeMesure(borderParams.getCornerLeftTopSize());
        cornerLeftBottomSize = layoutParams.computeMesure(borderParams.getCornerLeftBottomSize());
        cornerRightTopSize = layoutParams.computeMesure(borderParams.getCornerRightTopSize());
        cornerRightBottomSize = layoutParams.computeMesure(borderParams.getCornerRightBottomSize());
    }

    private void drawDebugBox() {
        g.setColor(debugColor);
        GL11.glBegin(GL11.GL_LINE_LOOP);

        
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(right, top);
        GL11.glVertex2d(right, top + height);
        GL11.glVertex2d(left, top + height);
        GL11.glEnd();
    }

}
