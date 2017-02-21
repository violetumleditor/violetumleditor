/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.workspace.sidebar;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

public class ScrollPaneNavigatorPanel extends JPanel
{
    private static final double MAX_SIZE = 200;
    private JScrollPane theScrollPane;
    private JComponent theComponent;
    private BufferedImage theImage;
    private Rectangle theStartRectangle;
    private Rectangle theRectangle;
    private Point theStartPoint;
    private double theScale;

    public ScrollPaneNavigatorPanel(JScrollPane aScrollPane)
    {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        theScrollPane = aScrollPane;
        theComponent = (JComponent) theScrollPane.getViewport().getView();
        theImage = null;
        theStartRectangle = null;
        theRectangle = null;
        theStartPoint = null;
        theScale = 0.0;
        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        MouseInputListener mil = new MouseInputAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                theStartPoint = e.getPoint();
            }

            public void mouseReleased(MouseEvent e)
            {
                if (theStartPoint != null)
                {
                    Point newPoint = e.getPoint();
                    int deltaX = (int) ((newPoint.x - theStartPoint.x) / theScale);
                    int deltaY = (int) ((newPoint.y - theStartPoint.y) / theScale);
                    scroll(deltaX, deltaY);
                }
                theStartPoint = null;
                theStartRectangle = theRectangle;
            }

            public void mouseDragged(MouseEvent e)
            {
                if (theStartPoint == null) return;
                Point newPoint = e.getPoint();
                moveRectangle(newPoint.x - theStartPoint.x, newPoint.y - theStartPoint.y);
            }
        };
        addMouseListener(mil);
        addMouseMotionListener(mil);
        // thePopupMenu = new JPopupMenu();
        // thePopupMenu.setLayout(new BorderLayout());
        // thePopupMenu.add(this, BorderLayout.CENTER);
        // thePopupMenu.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new BorderLayout());

        theScrollPane.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent arg0)
            {
                display();
            }
        });
    }

    protected void paintComponent(Graphics g1D)
    {
        if (theImage == null || theRectangle == null) return;
        Graphics2D g = (Graphics2D) g1D;
        Insets insets = getInsets();
        int xOffset = insets.left;
        int yOffset = insets.top;
        int availableWidth = getWidth() - insets.left - insets.right;
        int availableHeight = getHeight() - insets.top - insets.bottom;
        g.drawImage(theImage, xOffset, yOffset, null);
        Color tmpColor = g.getColor();
        Area area = new Area(new Rectangle(xOffset, yOffset, availableWidth, availableHeight));
        area.subtract(new Area(theRectangle));
        g.setColor(new Color(255, 255, 255, 128));
        g.fill(area);
        g.setColor(Color.BLACK);
        g.draw(theRectangle);
        g.setColor(tmpColor);
    }

    public Dimension getPreferredSize()
    {
        if (theImage == null || theRectangle == null) return new Dimension();
        Insets insets = getInsets();
        return new Dimension(theImage.getWidth(null) + insets.left + insets.right, theImage.getHeight(null) + insets.top
                + insets.bottom);
    }

    private void display()
    {
        double compWidth = theComponent.getWidth();
        double compHeight = theComponent.getHeight();
        double scaleX = MAX_SIZE / compWidth;
        double scaleY = MAX_SIZE / compHeight;
        theScale = Math.min(scaleX, scaleY);

        theImage = new BufferedImage((int) (theComponent.getWidth() * theScale), (int) (theComponent.getHeight() * theScale),
                BufferedImage.TYPE_BYTE_BINARY);

        Graphics2D g = theImage.createGraphics();
        g.scale(theScale, theScale);
        theComponent.paint(g);

        theStartRectangle = theComponent.getVisibleRect();
        Insets insets = getInsets();
        theStartRectangle.x = (int) (theScale * theStartRectangle.x + insets.left);
        theStartRectangle.y = (int) (theScale * theStartRectangle.y + insets.right);
        theStartRectangle.width *= theScale;
        theStartRectangle.height *= theScale;
        theRectangle = theStartRectangle;

        // Dimension pref = thePopupMenu.getPreferredSize();
        //
        // thePopupMenu.show(theButton,
        // (theButton.getWidth() - pref.width) / 2,
        // (theButton.getHeight() - pref.height) / 2);
        repaint();

        try
        {
            Robot robot = new Robot();
            Point centerPoint = new Point(theRectangle.x + theRectangle.width / 2, theRectangle.y + theRectangle.height / 2);
            SwingUtilities.convertPointToScreen(centerPoint, this);
            robot.mouseMove(centerPoint.x, centerPoint.y);
        }
        catch (AWTException e)
        {
            e.printStackTrace();
        }
    }

    private void moveRectangle(int aDeltaX, int aDeltaY)
    {
        if (theStartRectangle == null) return;
        Insets insets = getInsets();
        Rectangle newRect = new Rectangle(theStartRectangle);
        newRect.x += aDeltaX;
        newRect.y += aDeltaY;
        newRect.x = Math.min(Math.max(newRect.x, insets.left), getWidth() - insets.right - newRect.width);
        newRect.y = Math.min(Math.max(newRect.y, insets.right), getHeight() - insets.bottom - newRect.height);
        Rectangle clip = new Rectangle();
        Rectangle.union(theRectangle, newRect, clip);
        clip.grow(2, 2);
        theRectangle = newRect;
        paintImmediately(clip);
    }

    private void scroll(int aDeltaX, int aDeltaY)
    {
        JComponent component = (JComponent) theScrollPane.getViewport().getView();
        Rectangle rect = component.getVisibleRect();
        rect.x += aDeltaX;
        rect.y += aDeltaY;
        component.scrollRectToVisible(rect);
        // thePopupMenu.setVisible(false);
    }

    // public static void main(String[] args) {
    // EventQueue.invokeLater(new Runnable() {
    // public void run() {
    // try {
    // JFrame frame = new JFrame(ScrollPaneBidule.class.getName());
    // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // URL imageURL = new URL("http://www.aboutstonehenge.info/images/education/stonehenge-wallpaper-1.jpg");
    // JLabel label = new JLabel(new ImageIcon(ImageIO.read(imageURL)));
    // JScrollPane scrollPane = new JScrollPane(label);
    // new ScrollPaneBidule(scrollPane);
    // frame.setContentPane(scrollPane);
    // frame.pack();
    // frame.setVisible(true);
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // });
    // }

}
