package rules;

import model.Ball;
import java.util.*;

public class CompositeSequenceRule implements SequenceRule {
    private final List<SequenceRule> _rules = new ArrayList<>();


    public CompositeSequenceRule(SequenceRule... rules) {
        _rules.addAll(Arrays.asList(rules));
    }

    public void addRule(SequenceRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("Rule cannot be null");
        }
        if (_rules.contains(rule)) {
            throw new IllegalStateException("Rule already exists: " + rule.getClass().getSimpleName());
        }
        _rules.add(rule);
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