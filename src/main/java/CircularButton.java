import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class CircularButton extends JButton {
    private float alpha = 1.0f; // 초기 투명도

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 정사각형 버튼 영역 내에 원 모양의 버튼 그리기
        int size = Math.min(getWidth(), getHeight());
        Shape shape = new Ellipse2D.Float(0, 0, size, size);
        g2d.clip(shape);

        // 투명도 적용
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        super.paintComponent(g2d);

        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        // 정사각형으로 버튼 크기 조절
        int size = Math.min(super.getPreferredSize().width, super.getPreferredSize().height);
        return new Dimension(size, size);
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public CircularButton(Icon icon) {
        super(icon);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
    }
}
