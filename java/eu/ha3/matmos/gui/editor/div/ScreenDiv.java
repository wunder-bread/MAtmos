package eu.ha3.matmos.gui.editor.div;

/**
 * @author dags_ <dags@dags.me>
 */

public class ScreenDiv extends Div
{
    private int width = 0;
    private int height = 0;

    public ScreenDiv(int width, int height)
    {
        super(1F, 1F, 0F, 0F);
        this.width = width;
        this.height = height;
    }

    public ScreenDiv setDimensions(int width, int height)
    {
        this.width = width;
        this.height = height;
        return this;
    }

    public final void drawScreen()
    {
        super.onDraw(0, 0, width, height);
    }

    @Override
    public void onMouseMove(int mouseX, int mouseY)
    {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button)
    {
        super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void onKeyType(char c, int code)
    {
        super.keyTyped(c, code);
    }

    @Override
    public void draw(int left, int top, int right, int bottom)
    {
        super.draw(left, top, right, bottom);
    }
}
