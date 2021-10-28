/*
TODO
    Get code working again
    Add double compatibility to velocities
    Implement position tracking for singular entities
    Add a /ms help code information section
    Add a command that gives a command block structured with an existing command that summons a tnt
    Clean up code



 */

package me.triadtriangle.multisummon;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Arrays;


public final class MultiSummon extends JavaPlugin {

    //Valid values allowed in the command block
    final String BLOCKTYPE_COMMAND_PREFIX = "blocktype";
    final String[] VALID_BLOCKTYPE_COMMAND_VALUES = {
            "tnt",
            "sand",
            "redsand",
            "gravel",
            "anvil",
            "dragon_egg",
            "concrete",
            "white_concrete",
            "orange_concrete",
            "magenta_concrete",
            "light_blue_concrete",
            "yellow_concrete",
            "lime_concrete",
            "pink_concrete",
            "gray_concrete",
            "light_gray_concrete",
            "cyan_concrete",
            "purple_concrete",
            "blue_concrete",
            "brown_concrete",
            "green_concrete",
            "red_concrete",
            "black_concrete"
    };
    final String[] VARIABLE_COMMAND_PREFIXES = {
            "y",
            "x",
            "z",
            "yv",
            "vy",
            "xv",
            "vx",
            "zv",
            "vz",
            "fuse",
            "amount"
    };


    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("MultiSummon Enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //if the sender is instance of a command block, handle it for a command block
        if (sender instanceof BlockCommandSender
                && args.length > 3
                && Arrays.stream(args).anyMatch(arg -> arg.contains("blocktype:"))
                && Arrays.stream(args).anyMatch(arg -> arg.contains("x:"))
                && Arrays.stream(args).anyMatch(arg -> arg.contains("y:"))
                && Arrays.stream(args).anyMatch(arg -> arg.contains("z:"))
                && isCommandValid(args)) {

            //TODO
            //System.out.println("Command has been validitified");

            //getting variables
            BlockCommandSender commandBlock = (BlockCommandSender) sender;
            World world = commandBlock.getBlock().getWorld();
            BlockData block;

            //variables
            String blockType = "";
            int spawnAmount = 1;
            int fuseTime = 80;

            //creating a new velocity to later make all the summoned PrimedTNT not have any velocity for the first game tick of summoning
            Vector velocity = new Vector(0.0d, 0.0d, 0.0d);

            //set the location specified by the given coordinates
            Location location = new Location(world, 0.0d, 0.0d, 0.0d);

            //setting variables to their argument equivalents
            for (String argument : args) {

                //get the command prefix as lowercase without the colon
                switch (getAlternateSpelling(argument.substring(0, argument.indexOf(":")).toLowerCase())) {

                    //set locations and fix positions to center if the value is an integer
                    case "x":
                        if (isStringAInteger(getStringCommandValue(argument))) {

                            location.setX(returnCenterAlignedPosition(getStringCommandValue(argument)));
                            break;

                        }
                        location.setX(Double.parseDouble(getStringCommandValue(argument)));
                        break;

                    case "y":
                        if (isStringAInteger(getStringCommandValue(argument))) {

                            location.setY(returnCenterAlignedPosition(getStringCommandValue(argument)));
                            break;

                        }
                        //if the location is not an integer, then set position as a double
                        location.setY(Double.parseDouble(getStringCommandValue(argument)));
                        break;

                    case "z":
                        if (isStringAInteger(getStringCommandValue(argument))) {

                            location.setZ(returnCenterAlignedPosition(getStringCommandValue(argument)));
                            break;

                        }
                        //if the location is not an integer, then set position as a double
                        location.setZ(Double.parseDouble(getStringCommandValue(argument)));
                        break;

                        //set spawn amount
                    case "amount":
                        spawnAmount = Integer.parseInt(getStringCommandValue(argument));
                        break;

                        //set blocktype
                    case "blocktype":
                        blockType = getAlternateSpelling(getStringCommandValue(argument));
                        break;

                        //set fuse time
                    case "fuse":
                        fuseTime = Integer.parseInt(getStringCommandValue(argument));
                        break;

                        //set velocities
                    case "xv":
                        velocity.setX(Double.parseDouble(getStringCommandValue(argument)));
                        break;

                    case "yv":
                        velocity.setY(Double.parseDouble(getStringCommandValue(argument)));
                        break;

                    case "zv":
                        velocity.setZ(Double.parseDouble(getStringCommandValue(argument)));
                        break;

                        //if this get's reached, then there is a problem with getting the prefixes or validity in the command
                    default:
                        System.out.println("No command prefix in command argument: " + argument);
                        break;

                }//switch

            }//for each

            /*
            by this point
                command has been validated
                fuse time is set
                spawn amount is set
                locations are set
                locations have been centered if given an Integer
                velocities are set
                blocktype is set

             */

            //switch statement to check the blockType to see how to handle the spawning of blocks
            switch (blockType){

                case "tnt":
                    spawnPrimedTnT(location,world,velocity,spawnAmount,fuseTime);
                    break;
                case "sand":
                    block = Material.SAND.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "redsand":
                    block = Material.RED_SAND.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "gravel":
                    block = Material.GRAVEL.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "anvil":
                    block = Material.ANVIL.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "dragon_egg":
                    block = Material.DRAGON_EGG.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                //if command has "concrete" instead of "white_concrete", then change it in to code to be "concrete"
                case "white_concrete":
                    block = Material.WHITE_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "orange_concrete":
                    block = Material.ORANGE_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "magenta_concrete":
                    block = Material.MAGENTA_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "light_blue_concrete":
                    block = Material.LIGHT_BLUE_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "yellow_concrete":
                    block = Material.YELLOW_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "lime_concrete":
                    block = Material.LIME_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "pink_concrete":
                    block = Material.PINK_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "gray_concrete":
                    block = Material.GRAY_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "light_gray_concrete":
                    block = Material.LIGHT_GRAY_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "cyan_concrete":
                    block = Material.CYAN_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "purple_concrete":
                    block = Material.PURPLE_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "blue_concrete":
                    block = Material.BLUE_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "brown_concrete":
                    block = Material.BROWN_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "green_concrete":
                    block = Material.GREEN_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "red_concrete":
                    block = Material.RED_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "black_concrete":
                    block = Material.BLACK_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                default:
                    System.out.println("No blockType specified");
                    break;

            }//switch

        }//if (sender instanceof BlockCommandSender && args.length > 0)
        //else if the sender is of Player, then give how the command should be used
        else if (sender instanceof Player){
            sender.sendMessage("Type \"/ms help\" for more information");
        }

        return true;
    }//onCommand @Override

    /*
    METHODS START HERE--------------------------------------------------------------------------------------------------
     */

    //spawn a falling block method
    public void spawnFallingBlock(Location location,
                                  BlockData block,
                                  World world,
                                  Vector velocity,
                                  int spawnAmount){

            //spawn multiple based on number given
            for (int i = 0; spawnAmount > i; i++) {

                //spawn the block
                FallingBlock entity = world.spawnFallingBlock(location, block);

                //TODO Setup a way to keep track of where the center of the falling block is every game tick

                //set the velocity of the entity to velocity
                entity.setVelocity(velocity);

            }//for

    }//spawnFallingBlock

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //spawn a falling block method
    public void spawnPrimedTnT(Location location,
                                  World world,
                                  Vector velocity,
                                  int spawnAmount,
                                  int fuseTime){

        //spawn multiple based on number given
        for (int i = 0; spawnAmount > i; i++) {

            //spawning in the primed tnt and parsing to a TNTPrimed to set specific variables
            TNTPrimed tntFused = (TNTPrimed) world.spawnEntity(location, EntityType.PRIMED_TNT);

            //TODO Setup a way to keep track of where the center of the tnt is every game tick

            //setting velocities to zero and setting fuse time
            tntFused.setVelocity(velocity);
            tntFused.setFuseTicks(fuseTime);

        }//for

    }//spawnPrimedTnT method

    //check if the sent command value can be parsed to a double
    public double parseCommandValue (String commandToCheck) {

        try {
            Double.parseDouble(commandToCheck);
        }
        catch (Exception e) {

            System.out.println(e.getMessage());
            return 0.0;

        }

        return Double.parseDouble(commandToCheck);

    }//checkForAllowedNumberCharacters

    //check if the command is a valid command, if not, return false
    public boolean isCommandValid (String[] commandArguments) {

        //TODO
        //System.out.println("Command is Being Validified");

        //loop through each argument in the command and check their validity, even if 1 is invalid then return false
        for (String commandArgument : commandArguments) {

            //TODO
            //System.out.println(commandArgument);

            //check if there is at least more than 2 characters in the argument, if not, return false
            if (commandArgument.length() > 2) {

                //make commandBeingCheck to lowercase
                commandArgument = commandArgument.toLowerCase();

                String commandValue;

                //the command prefix
                String commandPrefix;

                //where the colon for the command is located in commandBeingChecked
                int colonPrefixIndex;

                //amount of colons in commandBeingChecked
                int amountOfColons = 0;


                //loop through each character in commandBeingChecked to see if it has at least 1 colon
                for (char charIterable : commandArgument.toCharArray()) {

                    //if commandBeingChecked contains a colon, then take out the first colon and add 1 to amountOfColons
                    if (charIterable == ':') {

                        amountOfColons++;

                    }

                    //if the commandBeingChecked has more than 1 colon then the command is not valid, so return false
                    if (amountOfColons > 1) {

                        return false;

                    }

                }

                //if the command has no colons, it is not a valid command, so return false
                if (amountOfColons == 0) {

                    return false;

                }

                //TODO
                //System.out.println("Number of colons: " + amountOfColons);

                //Keeping track of where the colon is in the command
                colonPrefixIndex = commandArgument.indexOf(":");

                //grabbing only the prefix of the command with the colon, to check if it's a valid command prefix
                //have to have + 1 on the endPoint of the substring because you want to include the colon
                commandPrefix = commandArgument.substring(0, colonPrefixIndex + 1);

                //if the command prefix before the colon is empty, then the command is invalid, so return false
                if (commandPrefix.length() < 2) {

                    return false;

                }

                //by this point the command should have a command prefix of something similar to "x:"
                //or "as543dvfgs:" the last one is not valid, that will be checked for validity later in the code

                //if there is no value after the command prefix, the command is invalid, so return false
                if (commandPrefix.length() == commandArgument.length()) {

                    return false;

                }

                //setting commandPrefix to the command's prefix without a colon
                commandPrefix = commandArgument.substring(0, colonPrefixIndex);

                //the command value is any value after the command prefix
                commandValue = commandArgument.substring(colonPrefixIndex + 1);

                //if the command prefix is equal to the blocktype command prefix then compare the commandValue to valid BlockType command values
                if (commandPrefix.equalsIgnoreCase(BLOCKTYPE_COMMAND_PREFIX)) {

                    //boolean to check if commandValue has a match in VALID_BLOCKTYPE_COMMAND_VALUES
                    boolean commandValueMatchesAValidCommandValue = false;

                    //for each valid command value, check if the commandValue is equal to any of the valid command values
                    for (String validCommandValue : VALID_BLOCKTYPE_COMMAND_VALUES) {

                        if (commandValue.equalsIgnoreCase(validCommandValue)) {

                            commandValueMatchesAValidCommandValue = true;
                            break;

                        }//if

                    }//for

                    //if commandValue does not match a valid command value, then return false
                    if (!(commandValueMatchesAValidCommandValue)) {

                        return false;

                    }//if

                }//if
                //else if the commandPrefix is not of the blocktype command prefix, check if it's a valid other prefix
                else {

                    //checks to see if the prefix is valid
                    boolean isValidVariableCommandPrefix = false;

                    //check if the commandPrefix is valid, and if the command value can be parsed
                    for (String validCommandPrefix : VARIABLE_COMMAND_PREFIXES) {

                        if (commandPrefix.equalsIgnoreCase(validCommandPrefix)) {

                            isValidVariableCommandPrefix = true;

                            //if parsing the command value does not work, then the command value is invalid, so return false
                            try {

                                Double.parseDouble(commandValue);

                            } catch (Exception e) {

                                System.out.println(e);
                                return false;

                            }//try catch

                        }//if

                    }//for each

                    //if the commandPrefix does not match any command prefixes, then it's not a valid command so return false
                    if (!(isValidVariableCommandPrefix)) {

                        //return false if the command prefix does not match any valid command prefixes
                        return false;

                    }//if

                }//else

            }//if command being checked is less than 2
            else {

                //if the command does not have enough syntax, then return false
                return false;

            }//else

        }//foreach

        //if the code has not returned false by this point, then return true
        return true;

    }//isCommandValid method

    //returns anything after the ":" in a given string
    public String getStringCommandValue (String commandArgument) {

        return commandArgument.substring(commandArgument.indexOf(":") + 1);

    }

    //return set values for alternate command value spelling
    public String getAlternateSpelling(String commandValue) {

        commandValue = commandValue.toLowerCase();

        switch (commandValue) {

            case "concrete":
                return "white_concrete";

            case "vx":
                return "xv";

            case "vy":
                return "yv";

            case "vz":
                return "zv";

            default:
                return commandValue;

        }//switch

    }//checkAlternateCommandValueSpelling

    //checks if a string is an integer or not, if it is, then return true, if not, return false
    public boolean isStringAInteger (String argumentValue) {

        return !argumentValue.contains(".");

    }//isStringAInteger

    //return a modified position value depending on if the positionValue is a positive or negative
    public double returnCenterAlignedPosition (Double positionValue) {

        if (positionValue > 0) {

            //if positionValue is a positive then the center of the block is + 0.5 from positionValue
            return positionValue + 0.5;

        }
        else if (positionValue < 0) {

            //if positionValue is a negative then the center of the block is - 0.5 from positionValue
            return positionValue - 0.5;

        }

        return positionValue;

    }//returnCenterAlignedPosition

    public double returnCenterAlignedPosition (String positionValue) {

        return returnCenterAlignedPosition(Double.parseDouble(positionValue));

    }//returnCenterAlignedPosition (double positionValue)

}//class