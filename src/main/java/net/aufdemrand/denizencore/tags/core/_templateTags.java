package net.aufdemrand.denizencore.tags.core;

import net.aufdemrand.denizencore.objects.TagRunnable;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.ReplaceableTagEvent;
import net.aufdemrand.denizencore.tags.TagManager;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.aufdemrand.denizencore.utilities.debugging.dB;

import java.util.ArrayList;
import java.util.List;

/**
 * Example replaceable tag class.
 * <p/>
 * This is very outdated / bad.
 * TODO: Update me!
 */
public class _templateTags {

    public _templateTags() {
        // Register this class with the TagManager event handler.
        TagManager.registerTagHandler(new TagRunnable.RootForm() {
            @Override
            public void run(ReplaceableTagEvent event) {
                skillTags(event);
            }
        }, "skills");
    }

    public void skillTags(ReplaceableTagEvent event) {
        // Since this event will be called each time Denizen comes across a
        // replaceable tag, something needs to tell Denizen if this is the
        // appropriate place to fill the tag. This is done by checking the
        // tag 'name'. Using event.matches(tag_name) will ensure that the name
        // of the tag is 'tag_name'. :)
        // ie. <skills.something...> .. the tag name would be 'skills'
        if (!event.matches("skills")) {
            return;
        }

        // Your event may need to fetch some information.
        // Denizen will break down the first 4 parts of the tag to help identify
        // the intent: name, type, subtype, and specifier

        // It also has some helper methods to check if a part is present, and to
        // easily fetch the information.
        // ie. <name.type.subtype.specifier> ... to get the type easily, use:

        String type = event.hasType() ? event.getType() : "";
        // The other parts may be handled the same way.

        // The various parts may also have some 'context', which can be referenced easy.
        // Context to a type, subtype, specifier, etc. is contained in [] brackets.
        // ie. <tag_name.type[context]>
        String type_context = event.hasTypeContext() ? event.getTypeContext() : "";

        // For this example, let's process a tag in the format of: <skills.for[player_name]>
        // and return a dList dObject object to fulfill any additional attributes of the tag.
        if (type.equalsIgnoreCase("for")) {
            // Returning the results as another object allows for other attributes on the tag
            // to be filled.Returning a dList object, for example, allows attributes such as
            // .get[#] or .ascslist, but should at the very least return an Element, which
            // will handle such attributes as .asint, .split, .aslocation, etc.

            // Returning a dList will help with things like: <skills.for[player_name].size>
            // which can tell how many items are in the list, all without any additional
            // code to handle each situation. A full list of attributes can be found
            // in Denizen's documentation. First you need to turn the tag into an
            // attribute object.
            Attribute attribute = event.getAttributes();

            // Now to catch up, 2 attributes have been handled already...
            // Fulfilling 2 attributes, skills and .for, in <skills.for[player].get[1]>
            // will leave the .get[1] to be handled by the dList.
            attribute.fulfill(2);

            List<String> skills = new ArrayList<String>();

            // skills = Skills.getForPlayer(aH.getPlayerFrom(type_context)).list()

            // Use event.setReplaced() to pass the attribute off to the dList object (or any other dScriptArg object).
            // The dList constructor requires a string list and a prefix.
            event.setReplacedObject(CoreUtilities.autoAttrib(new dList(skills), attribute));
        }

        // Got here? No attributes were handled! Probably should let the dBugger know.
        dB.echoError("Example skills tag '" + event.raw_tag
                + "' was unable to match an attribute. Replacement has been cancelled...");

    }
}
