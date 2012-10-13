package com.irr310.i3d.view;

import org.lwjgl.opengl.GL11;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Color;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.I3dContext;
import com.irr310.i3d.Texture;
import com.irr310.i3d.fonts.CharacterPixmap;
import com.irr310.i3d.fonts.Font;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;

public class TextView extends View {

    protected String text = "plop";
    protected Font font;
    private String[] wrappedText;
    protected Color textColor = Color.black;

    public TextView(Graphics g) {
        super(g);
        font = I3dContext.getInstance().getDefaultFont();
        wrappedText = new String[0];

        // wrappedText[0] = "Bonjour ceci est un test de text assez long.";
        // wrappedText[1] = "plop";
    }

    @Override
    public void onDraw() {

        // int localX = x + g.getTranslation().getX();
        // int localXbase = localX;
        // int localY = y + g.getTranslation().getY() - getLineHeight();

        int localX = 0;
        int localXbase = localX;
        int localY = 0;

        g.setColor(textColor);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        CharacterPixmap pixmap;

        boolean init = true;

        for (String text : wrappedText) {
            for (int i = 0; i < text.length(); i++) {
                final char c = text.charAt(i);
                if (c == '\r' || c == '\f' || c == '\t')
                    continue;
                else if (c == ' ') {
                    localX += font.getWidth(' ');
                    continue;
                }
                pixmap = font.getCharPixMap(c);

                if (init) {
                    Texture tex = pixmap.getTexture();

                    if (tex != null) {
                        if (tex.hasAlpha()) {
                            GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
                        }

                        tex.bind();
                    }
                    GL11.glBegin(GL11.GL_QUADS);
                    init = false;
                }

                final int imgWidth = pixmap.getWidth();
                final int imgHeight = pixmap.getHeight();

                final float endY = pixmap.getEndY();
                final float endX = pixmap.getEndX();

                final float startX = pixmap.getStartX();
                final float startY = pixmap.getStartY();

                GL11.glTexCoord2f(startX, startY);
                GL11.glVertex2i(localX, localY);

                GL11.glTexCoord2f(startX, endY);
                GL11.glVertex2i(localX, imgHeight + localY);

                GL11.glTexCoord2f(endX, endY);
                GL11.glVertex2i(imgWidth + localX, imgHeight + localY);

                GL11.glTexCoord2f(endX, startY);
                GL11.glVertex2i(imgWidth + localX, localY);

                localX += pixmap.getCharWidth();
            }
            // move to start of next line
            localY += font.getHeight();
            localX = localXbase;
        }
        if (!init)
            GL11.glEnd();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    @Override
    public View duplicate() {
        TextView view = new TextView(g);
        view.setId(getId());
        view.setTextColor(textColor);
        view.setLayout(getLayoutParams().duplicate());
        view.setText(text);
        view.setFont(font);
        return view;
    }

    public void setText(String text) {
        this.text = text;
        wrappedText = new String[1];
        wrappedText[0] = text;
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {

    }

    public void setFont(Font font) {
        if (font != null) {
            this.font = font;
        }
    }

    @Override
    public void onMeasure() {
        float measuredWidth = 0;
        float measuredHeight = font.getHeight();

        CharacterPixmap pixmap;

        for (int i = 0; i < text.length(); i++) {
            final char c = text.charAt(i);
            if (c == '\r' || c == '\f' || c == '\t')
                continue;
            else if (c == ' ') {
                measuredWidth += font.getWidth(' ');
                continue;
            }
            pixmap = font.getCharPixMap(c);

            measuredWidth += pixmap.getCharWidth();
        }
        Log.trace("TextView onMeasure "+ text);
        Log.trace("measuredWidth "+measuredWidth);
        Log.trace("measuredHeight "+measuredHeight);
        
        if(!layoutParams.getLayoutMarginTop().isRelative()) {
            measuredHeight +=   layoutParams.computeMesure(layoutParams.getLayoutMarginTop());  
        }
        if(!layoutParams.getLayoutMarginBottom().isRelative()) {
            measuredHeight +=   layoutParams.computeMesure(layoutParams.getLayoutMarginBottom());  
        }
        if(!layoutParams.getLayoutMarginLeft().isRelative()) {
            measuredWidth +=   layoutParams.computeMesure(layoutParams.getLayoutMarginLeft());  
        }
        if(!layoutParams.getLayoutMarginRight().isRelative()) {
            measuredWidth +=   layoutParams.computeMesure(layoutParams.getLayoutMarginRight());  
        }
        

        if(layoutParams.getLayoutWidthMeasure() != LayoutMeasure.FIXED || layoutParams.getMeasurePoint().getX().isRelative()) {
            layoutParams.mContentWidth = measuredWidth;
        }
        if(layoutParams.getLayoutHeightMeasure() != LayoutMeasure.FIXED || layoutParams.getMeasurePoint().getY().isRelative()) {
            layoutParams.mContentHeight = measuredHeight;
        }
    }
}
