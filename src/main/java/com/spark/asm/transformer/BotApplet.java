package com.spark.asm.transformer;

import java.applet.Applet;
import java.awt.*;

public class BotApplet extends Applet {

    private boolean threadStarted = false;

    public BotApplet() {
        super();
    }

    @Override
    public Graphics getGraphics() {
        final Graphics g = super.getGraphics();
        if (!threadStarted) {
            Thread thread = new Thread(() -> {
                g.setColor(Color.RED);
                g.drawString("Some string here!", 100, 100);
            });

            threadStarted = true;
            thread.start();
        }

        // We eventually have to return the Graphics object, since that's what the RSApplet class wanted in the first place
        return g;
    }
}
