package systems.conduit.main.core.permissions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Innectic
 * @since 12/14/2020
 */
@AllArgsConstructor
public class PermissionNode {

    @Getter private String permission;

    public boolean applies(String to) {
        // If they're just the same thing, then it definitely applies.
        if (to.equalsIgnoreCase(permission)) return true;

        // Next attempt to resolve a wildcard on this.
        Iterator<String> checkingParts = Arrays.asList(to.split("\\.")).iterator();
        Iterator<String> againstParts = Arrays.asList(permission.split("\\.")).iterator();

        while(checkingParts.hasNext() && againstParts.hasNext()) {
            String checkPart = checkingParts.next();
            String againstPart = againstParts.next();

            if (!againstParts.hasNext() && againstPart.equals("*")) return true;
            if (!againstPart.equals("*") && !againstPart.equalsIgnoreCase(checkPart)) return false;
        }

        return false;
    }
}
