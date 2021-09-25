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


public final class MultiSummon extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("MutliSummon Enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //if the sender is instance of a command block, handle it for a command block
        if (sender instanceof BlockCommandSender && args.length > 0) {


            //getting variables
            BlockCommandSender commandBlock = (BlockCommandSender) sender;
            World world = commandBlock.getBlock().getWorld();
            BlockData block;
            String blockType = args[3];
            int spawnAmount = Integer.parseInt(args[4]);
            int[] velocities = {0,0,0};
            int fuseTime = 80;

            //if tnt is specified to spawn, then add fuse time, but if anything else, it's probably
            //something that doesn't need a fuse time, so only add fuse time if tnt is specified to spawn
            if (blockType.equalsIgnoreCase("tnt")) {

                fuseTime = Integer.parseInt(args[5]);

            }//if

            //check if there are velocities given in the args[] array
            //if so, then give them the requested values
            //else if the blockType is sand, the args[] index positions change by -1
            if (args.length > 5 && blockType.equalsIgnoreCase("sand")
                                || blockType.equalsIgnoreCase("redsand")
                                || blockType.equalsIgnoreCase("concrete")){

                switch (args.length) {

                    case 6:
                        velocities[0] = Integer.parseInt(args[5]);
                        break;
                    case 7:
                        velocities[0] = Integer.parseInt(args[5]);
                        velocities[1] = Integer.parseInt(args[6]);
                        break;
                    case 8:
                        velocities[0] = Integer.parseInt(args[5]);
                        velocities[1] = Integer.parseInt(args[6]);
                        velocities[2] = Integer.parseInt(args[7]);
                        break;

                    default: break;

                }//switch

            }//if
            //else if tnt, the args[] indexes change by +1 because fuseTime is added
            else if (args.length > 6 && blockType.equalsIgnoreCase("tnt")) {

                switch (args.length) {

                    case 7:
                        velocities[0] = Integer.parseInt(args[6]);
                        break;
                    case 8:
                        velocities[0] = Integer.parseInt(args[6]);
                        velocities[1] = Integer.parseInt(args[7]);
                        break;
                    case 9:
                        velocities[0] = Integer.parseInt(args[6]);
                        velocities[1] = Integer.parseInt(args[7]);
                        velocities[2] = Integer.parseInt(args[8]);
                        break;

                    default: break;

                }//swtich

            }//else if


            //creating a new velocity to later make all the summoned PrimedTNT not have any velocity for the first gametick of summoning
            Vector velocity = new Vector(velocities[0],velocities[1],velocities[2]);

            //setting specific locations
            double[] coordinates = {
                    Double.parseDouble(args[0]),
                    Double.parseDouble(args[1]),
                    Double.parseDouble(args[2])};

            //if the coordinate is negative or positive, offset it accordingly so it spawns in the center
            //of the given coordinate instead of out of it.
            //if the position does not contain a decimal, then offset the position by .5
            // to spawn in the center of the specified coordinate
            if (coordinates[0] >= 0 && coordinates[2] >= 0) {

                if (!args[0].contains(".")){coordinates[0] += 0.5;}

                if (!args[2].contains(".")){coordinates[2] += 0.5;}

            }//if
            else if (coordinates[0] < 0 && coordinates[2] < 0) {

                if (!args[0].contains(".")){coordinates[0] -= 0.5;}

                if (!args[2].contains(".")){coordinates[2] -= 0.5;}

            }//else if
            else if (coordinates[0] < 0 && coordinates[2] > 0) {

                if (!args[0].contains(".")){coordinates[0] -= 0.5;}

                if (!args[2].contains(".")){coordinates[2] += 0.5;}

            }//else
            else {

                if (!args[0].contains(".")){coordinates[0] += 0.5;}

                if (!args[2].contains(".")){coordinates[2] -= 0.5;}

            }//else if

            //set the location specified by the given coordinates
            Location location = new Location(world, coordinates[0], coordinates[1], coordinates[2]);

            //switch statement to check the blockType to see how to handle the spawning of blocks
            switch (blockType){

                case "sand":
                    block = Material.SAND.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "redsand":
                    block = Material.RED_SAND.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "concrete":
                    block = Material.WHITE_CONCRETE_POWDER.createBlockData();
                    spawnFallingBlock(location,block,world,velocity,spawnAmount);
                    break;
                case "tnt":
                        spawnPrimedTnT(location,world,velocity,spawnAmount,fuseTime);
                    break;


                default:
                    System.out.println("No blockType specified");
                    break;

            }//switch




            //commeted out for now, making a switch case statement to simplify this process
            /*
            //if blockType is equal to sand, make and spawn a certain amount of sand blocks
            if (blockType.equalsIgnoreCase("sand")) {

                //make the block a sand block
                block = Material.SAND.createBlockData();

                //spawnFallingBlock method to spawn in the entities
                spawnFallingBlock(location, block, world, velocity, spawnAmount, blockType);

            }//if (blockType.equalsIgnoreCase("sand"))

            //if blockType is equal to red sand, make and spawn a certain amount of sand blocks
            else if (blockType.equalsIgnoreCase("redsand")) {

                //make the block a sand block
                block = Material.RED_SAND.createBlockData();



            }//if (blockType.equalsIgnoreCase("sand"))

            //if blockType is equal to red sand, make and spawn a certain amount of sand blocks
            else if (blockType.equalsIgnoreCase("concrete")) {

                //make the block a sand block
                block = Material.WHITE_CONCRETE_POWDER.createBlockData();

                //spawn multiple based on number given
                for (int i = 0; Integer.parseInt(args[4]) > i; i++) {

                    //spawn the block
                    FallingBlock powder = world.spawnFallingBlock(location, block);

                    //set the velocity of the sand to the velocity give or pre-set
                    powder.setVelocity(velocity);

                }//for

            }//if (blockType.equalsIgnoreCase("sand"))

            //if blockType is equal to sand, make and spawn a certain amount of tnt primed
            else if (blockType.equalsIgnoreCase("tnt")) {

                //spawn multiple based on number given
                for (int i = 0; Integer.parseInt(args[4]) > i; i++) {

                    //spawning in the primed tnt and parsing to a TNTPrimed to set specific variables
                    Entity tntNoFuse = world.spawnEntity(location, EntityType.PRIMED_TNT);
                    TNTPrimed tntFused = (TNTPrimed) tntNoFuse;

                    //setting velocities to zero and setting fuse time
                    tntFused.setVelocity(velocity);
                    tntFused.setFuseTicks(fuseTime);

                }//for

            }//if (blockType.equalsIgnoreCase("sand"))

             */


        }//if (sender instanceof BlockCommandSender && args.length > 0)

        else if (sender instanceof Player){
            sender.sendMessage("/ms xPos yPos zPos blockType spawnAmount fuseTime xVelocity yVelocity zVelocity");
        }

        return true;
    }//onCommand @Override









    /*
    METHODS START HERE
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
            Entity tntNoFuse = world.spawnEntity(location, EntityType.PRIMED_TNT);
            TNTPrimed tntFused = (TNTPrimed) tntNoFuse;

            //setting velocities to zero and setting fuse time
            tntFused.setVelocity(velocity);
            tntFused.setFuseTicks(fuseTime);

        }//for

    }//spawnPrimedTnT method







}//class
