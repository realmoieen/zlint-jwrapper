package com.moieen.zlint.jwrapper;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * this class is the result of lints that has some extra functionality like all lints are valid
 */
public class LintResult {

    private final Collection<Lint> lints;

    public LintResult(Collection<Lint> lints) {
        if (lints == null || lints.isEmpty()) {
            this.lints = Collections.emptyList();
        } else {

            this.lints = Collections.unmodifiableCollection(lints);
        }
    }

    public static Predicate<Lint> getErrorLintPredicate() {
        return lint -> "Error".equalsIgnoreCase(lint.getResult());
    }

    public static Predicate<Lint> getPassLintPredicate() {
        return lint -> "Pass".equalsIgnoreCase(lint.getResult());
    }

    public static Predicate<Lint> getNALintPredicate() {
        return lint -> "NA".equalsIgnoreCase(lint.getResult());
    }

    public static Predicate<Lint> getNoticeLintPredicate() {
        return lint -> "Notice".equalsIgnoreCase(lint.getResult());
    }

    public static Predicate<Lint> getWarnLintPredicate() {
        return lint -> "Warn".equalsIgnoreCase(lint.getResult());
    }

    /**
     * Returns the lints
     *
     * @return
     */
    public Collection<Lint> getLints() {
        return lints;
    }

    /**
     * Return true if there is no lints with result error
     *
     * @return
     */
    public boolean isPassed() {
        return lints.stream().noneMatch(getErrorLintPredicate());
    }

    /**
     * Returnt the list of lints with Error result
     *
     * @return
     */
    public Collection<Lint> getErrorLints() {
        return lints.stream().filter(getErrorLintPredicate()).collect(Collectors.toList());
    }
}
