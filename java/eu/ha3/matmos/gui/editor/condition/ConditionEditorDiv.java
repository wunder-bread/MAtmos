package eu.ha3.matmos.gui.editor.condition;

import com.google.common.base.Optional;
import eu.ha3.matmos.MAtmos;
import eu.ha3.matmos.engine.condition.Checkable;
import eu.ha3.matmos.engine.condition.ConditionParser;
import eu.ha3.matmos.engine.condition.ConditionSet;
import eu.ha3.matmos.game.MCGame;
import eu.ha3.matmos.gui.editor.div.Div;
import eu.ha3.matmos.util.Timer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dags_ <dags@dags.me>
 */

public class ConditionEditorDiv extends Div
{
    private final List<ConditionBuilder> lines = new ArrayList<ConditionBuilder>();
    private final Timer timer = new Timer();
    private final MAtmos engine;

    protected static int lineHeight = 20;

    private String conditionSetName = "";

    private int editorTop = 0;
    private int editorLeft = 0;
    private int editorRight = 0;
    private int editorBottom = 0;

    private boolean cursor = false;

    public ConditionEditorDiv(MAtmos mAtmos, float w, float h, float ml, float mt)
    {
        super(w, h, ml, mt);
        engine = mAtmos;
    }

    public Optional<ConditionSet> clearCurrent()
    {
        if (conditionSetName.length() == 0)
        {
            return Optional.absent();
        }
        List<String> statements = new ArrayList<String>();
        for (ConditionBuilder cb : lines)
        {
            statements.add(cb.get(false));
        }
        ConditionSet result = new ConditionParser(engine.dataManager).parse(conditionSetName, statements);
        conditionSetName = "Nothing Selected";
        lines.clear();
        return Optional.of(result);
    }

    public ConditionEditorDiv setConditionSet(ConditionSet set)
    {
        lines.clear();
        conditionSetName = set.getName();
        for (Checkable c : set.getConditions())
        {
            lines.add(new ConditionBuilder(engine, c.serialize()));
        }
        return this;
    }

    @Override
    public void onDraw(int mouseX, int mouseY, int left, int top, int right, int bottom)
    {
        drawBorderedBox(left, top, right, top + lineHeight, 0xFF333333, 0xFFFFFFFF);
        MCGame.drawString(conditionSetName, left + 5, top + 6, 0xFFFFFFFF);
        drawBorderedBox(left, top += 20, right, bottom, 0xDD666666, 0xFFFFFFFF);

        editorTop = top + 1;
        editorLeft = left + 1;
        editorRight = right - 1;
        editorBottom = bottom;

        List<ConditionBuilder> empty = new ArrayList<ConditionBuilder>();
        for (ConditionBuilder cb : lines)
        {
            if (cb.empty() && !cb.active)
            {
                empty.add(cb);
            }
            else
            {
                if (timer.getPeriodMs() > 500)
                {
                    timer.punchIn();
                    cursor = !cursor;
                }
                cb.hovered = mouseOver(mouseX, mouseY, editorLeft, top, editorRight, top + lineHeight);
                cb.drawBox(editorLeft, top, editorRight, top + lineHeight);
                cb.draw(cursor, left + 5, top + 6, editorRight);
                drawHorizontalLine(editorLeft + 1, editorRight - 2, top + lineHeight, 0xAAFFFFFF);
                top += (lineHeight + 1);
            }
        }
        lines.removeAll(empty);
        timer.punchOut();
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button)
    {
        if (mouseOver(mouseX, mouseY, editorLeft, editorTop, editorRight, editorBottom))
        {
            boolean noneSelected = true;
            for (ConditionBuilder cb : lines)
            {
                if (cb.hovered)
                {
                    cb.active = noneSelected;
                    noneSelected = false;
                }
                else if (cb.active)
                {
                    cb.active = false;
                }
            }
            if (noneSelected)
            {
                ConditionBuilder cb = new ConditionBuilder(engine);
                cb.active = true;
                lines.add(cb);
            }
        }
        else
        {
            for (ConditionBuilder cb : lines)
                cb.active = false;
        }
    }

    @Override
    public void onKeyType(char c, int code)
    {
        for (ConditionBuilder cb : lines)
        {
            if (cb.active)
            {
                cb.onKeyType(c, code);
                return;
            }
        }
    }
}