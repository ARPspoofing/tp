package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONSULTATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LAB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERFORMANCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHOTO;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECUR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddConsultationCommand;
import seedu.address.logic.commands.AddRecurCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.Consultation;
import seedu.address.model.event.Lab;
import seedu.address.model.event.Tutorial;

/**
 * Parses input arguments and creates a new AddRecur object
 */
public class AddRecurParser implements Parser<AddRecurCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddRecur
     * and returns an AddRecur object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddRecurCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_RECUR);
        //Make the user not create (lab or tutorial) and students with the same command
        if (!arePrefixesAbsent(argMultimap, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_PHOTO, PREFIX_ADDRESS, PREFIX_REMARK, PREFIX_PERFORMANCE,
                PREFIX_TAG)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddRecurCommand.MESSAGE_USAGE));
        }

        if ((!arePrefixesPresent(argMultimap, PREFIX_RECUR) || !argMultimap.getPreamble().isEmpty())) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddRecurCommand.MESSAGE_USAGE));
        }

        String name = " " + ParserUtil.parseRecurName(argMultimap.getValue(PREFIX_RECUR).get());

        if (!isLab(name) && !isTutorial(name) && !isConsultation(name)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddRecurCommand.MESSAGE_USAGE));
        }

        if (isLab(name)) {
            return parseEvent(name, PREFIX_LAB);
        } else if (isTutorial(name)){
            return parseEvent(name, PREFIX_TUTORIAL);
        } else {
            return parseEvent(name, PREFIX_CONSULTATION);
        }
    }

    /**
     * Checks to if the recurring event is a lab or tutorial or consultation.
     * Do a typecasting for the respective events.
     * @param newArgs
     * @param prefix
     * @return AddRecurCommand
     * @throws ParseException
     */
    public AddRecurCommand parseEvent(String newArgs, Prefix prefix) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(newArgs, prefix, PREFIX_COUNT);
        //Make the user not create (lab or tutorial) and students with the same command
        if (!arePrefixesAbsent(argMultimap, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                PREFIX_PHOTO, PREFIX_ADDRESS, PREFIX_REMARK, PREFIX_PERFORMANCE,
                PREFIX_TAG)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddRecurCommand.MESSAGE_USAGE));
        }
        int count = ParserUtil.parseRecurCount(argMultimap.getValue(PREFIX_COUNT).get());
        String name = ParserUtil.parseLabName(argMultimap.getValue(prefix).get());
        if (prefix.getPrefix().equals("Lab/")) {
            Lab lab = new Lab(name);
            return new AddRecurCommand(lab, true, false, false, count);
        } else if (prefix.getPrefix().equals("Tutorial/")){
            Tutorial tutorial = new Tutorial(name);
            return new AddRecurCommand(tutorial, false, true, false, count);
        } else {
            Consultation consultation = new Consultation(name);
            return new AddRecurCommand(consultation, false, false, true, count);
        }
    }

    boolean isLab(String newArgs) {
        System.out.println(newArgs.trim().split("/")[0].equals("Lab"));
        return newArgs.trim().split("/")[0].equals("Lab");
    }

    boolean isTutorial(String newArgs) {
        System.out.println(newArgs.trim().split("/")[0].equals("Tutorial"));
        return newArgs.trim().split("/")[0].equals("Tutorial");
    }

    boolean isConsultation(String newArgs) {
        System.out.println(newArgs.trim().split("/")[0].equals("Consultation"));
        return newArgs.trim().split("/")[0].equals("Consultation");
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Returns true if none of the prefixes contains command to add students (cannot add student and lab
     * using the same command.)
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesAbsent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).noneMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
