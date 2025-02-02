package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.AddConsultationCommand.MESSAGE_DUPLICATE_CONSULTATION;
import static seedu.address.logic.commands.AddLabCommand.MESSAGE_DUPLICATE_LAB;
import static seedu.address.logic.commands.AddTutorialCommand.MESSAGE_DUPLICATE_TUTORIAL;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Consultation;
import seedu.address.model.event.Event;
import seedu.address.model.event.Lab;
import seedu.address.model.event.Tutorial;

/**
 * Adds a recurring event to the address book.
 */
public class AddRecurCommand extends Command {

    public static final String COMMAND_WORD = "schedule";

    public static final String MESSAGE_USAGE_NO_STUDENT_PREFIX = "Cannot have prefixes related to students!";
    public static final String MESSAGE_USAGE_MISSING_RECUR_PREFIX = "Please enter the Recur/ prefix";
    public static final String MESSAGE_USAGE_MISSING_EVENT_PREFIX =
            "Please enter the Tutorial/ or Consultation/ or Lab/ prefix";

    public static final String MESSAGE_SUCCESS = "New recurring event added: %1$s";
    public static final String MESSAGE_DUPLICATE_RECUR = "This recurring event already exists in the address book";

    private final Event toAdd;
    private final int count;
    private final boolean lab;
    private final boolean tutorial;
    private final boolean consultation;

    /**
     * Creates an AddRecur to add the specified {@code Recur}
     */
    public AddRecurCommand(Event recurring, boolean lab, boolean tutorial, boolean consultation, int count) {
        requireNonNull(recurring);
        toAdd = recurring;
        this.count = count;
        this.lab = lab;
        this.tutorial = tutorial;
        this.consultation = consultation;
    }

    /**
     * Executes the model
     * @param model {@code Model} which the command should operate on.
     * @return CommandResult
     * @throws CommandException
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (tutorial && model.hasTutorial((Tutorial) toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_TUTORIAL);
        }

        if (lab && model.hasLab((Lab) toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_LAB);
        }

        if (consultation && model.hasConsultation((Consultation) toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_CONSULTATION);
        }

        Event newEvent = toAdd.copy();
        for (int i = 0; i < count; i++) {
            if (lab) {
                model.addLab((Lab) newEvent);
            } else if (tutorial) {
                model.addTutorial((Tutorial) newEvent);
            } else {
                model.addConsultation((Consultation) newEvent);
            }
            newEvent = newEvent.copy();
            LocalDateTime currDate = newEvent.getDate();
            LocalDateTime newDate = currDate.plus(1, ChronoUnit.WEEKS);
            newEvent.changeDate(newDate);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd), false, false, false, true);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddRecurCommand // instanceof handles nulls
                && toAdd.equals(((AddRecurCommand) other).toAdd));
    }
}
