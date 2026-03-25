package rules;

import model.Ball;
import java.util.*;

public class CompositeSequenceRule implements SequenceRule {
    private final List<SequenceRule> _rules = new ArrayList<>();


    public CompositeSequenceRule(SequenceRule... rules) {
        _rules.addAll(Arrays.asList(rules));
    }

    public void addRule(SequenceRule rule) {
        _rules.add(rule);
    }

    public void removeRule(SequenceRule rule) {
        _rules.remove(rule);
    }

    public void clearRules() {
        _rules.clear();
    }

    public List<SequenceRule> getRules() {
        return Collections.unmodifiableList(_rules);
    }

    @Override
    public boolean canStack(Ball topBall, Ball bottomBall) {
        for (SequenceRule rule : _rules) {
            if (!rule.canStack(topBall, bottomBall)) {
                return false;
            }
        }
        return true;
    }
}