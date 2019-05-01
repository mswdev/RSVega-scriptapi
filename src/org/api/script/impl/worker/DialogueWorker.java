package org.api.script.impl.worker;

import org.api.script.framework.worker.Worker;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Dialog;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class DialogueWorker extends Worker {

    private final Predicate<String> action;
    private final BooleanSupplier cutsceneDelay;

    public DialogueWorker() {
        this(a -> true);
    }

    public DialogueWorker(Predicate<String> action) {
        this(action, null);
    }

    public DialogueWorker(BooleanSupplier cutsceneDelay) {
        this(a -> true, cutsceneDelay);
    }

    public DialogueWorker(Predicate<String> action, BooleanSupplier cutsceneDelay) {
        this.action = action;
        this.cutsceneDelay = cutsceneDelay;
    }

    @Override
    public boolean needsRepeat() {
        return Dialog.isOpen();
    }

    @Override
    public void work() {
        if (cutsceneDelay != null && cutsceneDelay.getAsBoolean()) {
            processDialog();
            Time.sleepUntil(() -> !Dialog.isProcessing() && Dialog.isOpen(), 5000);
            return;
        }

        processDialog();
    }

    private void processDialog() {
        if (Dialog.canContinue())
            Dialog.processContinue();

        if (Dialog.isViewingChatOptions())
            Dialog.process(action);
    }

    @Override
    public String toString() {
        return "Executing dialogue worker.";
    }
}

