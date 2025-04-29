package org.KreativeName.calculator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Calculator extends JavaPlugin implements CommandExecutor, TabCompleter {

    @Override
    public void onEnable() {
        this.getCommand("calc").setExecutor(this);
        this.getCommand("coord").setExecutor(this);
        this.getCommand("distance").setExecutor(this);
        this.getCommand("stack").setExecutor(this);
        
        // Register tab completers
        this.getCommand("calc").setTabCompleter(this);
        this.getCommand("coord").setTabCompleter(this);
        this.getCommand("distance").setTabCompleter(this);
        this.getCommand("stack").setTabCompleter(this);
        
        getLogger().info("Calculator plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Calculator plugin disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used in-game.");
            return true;
        }

        Player player = (Player) sender;
        
        switch (command.getName().toLowerCase()) {
            case "calc":
                return handleCalcCommand(player, args);
            case "coord":
                return handleCoordCommand(player, args);
            case "distance":
                return handleDistanceCommand(player, args);
            case "stack":
                return handleStackCommand(player, args);
            default:
                return false;
        }
    }
    
    private boolean handleCalcCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /calc <expression>");
            return true;
        }

        String input = String.join(" ", args);
        try {
            Expression expression = new ExpressionBuilder(input).build();
            double result = expression.evaluate();
            player.sendMessage(ChatColor.GREEN + "Ergebnis: " + result);
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Ungültige Eingabe.");
        }
        return true;
    }
    
    private boolean handleCoordCommand(Player player, String[] args) {
        if (args.length != 4) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /coord [nether|overworld] <x> <y> <z>");
            return true;
        }
        
        try {
            String dimension = args[0].toLowerCase();
            double x = Double.parseDouble(args[1]);
            double y = Double.parseDouble(args[2]);
            double z = Double.parseDouble(args[3]);
            
            if (dimension.equals("nether")) {
                // Convert overworld coordinates to nether (divide by 8)
                double netherX = x / 8;
                double netherZ = z / 8;
                player.sendMessage(ChatColor.GREEN + "Nether coordinates: " + 
                                  ChatColor.GOLD + String.format("%.2f", netherX) + ", " + 
                                  String.format("%.2f", y) + ", " + 
                                  String.format("%.2f", netherZ));
            } else if (dimension.equals("overworld")) {
                // Convert nether coordinates to overworld (multiply by 8)
                double overworldX = x * 8;
                double overworldZ = z * 8;
                player.sendMessage(ChatColor.GREEN + "Overworld coordinates: " + 
                                  ChatColor.GOLD + String.format("%.2f", overworldX) + ", " + 
                                  String.format("%.2f", y) + ", " + 
                                  String.format("%.2f", overworldZ));
            } else {
                player.sendMessage(ChatColor.RED + " Unbekannte Dimension, bitte 'nether' oder 'overworld' angeben.");
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "ungültige Koordinaten");
        }
        return true;
    }
    
    private boolean handleDistanceCommand(Player player, String[] args) {
        if (args.length != 6) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /distance <x1> <y1> <z1> <x2> <y2> <z2>");
            return true;
        }
        
        try {
            double x1 = Double.parseDouble(args[0]);
            double y1 = Double.parseDouble(args[1]);
            double z1 = Double.parseDouble(args[2]);
            double x2 = Double.parseDouble(args[3]);
            double y2 = Double.parseDouble(args[4]);
            double z2 = Double.parseDouble(args[5]);
            
            double distance = Math.sqrt(
                Math.pow(x2 - x1, 2) + 
                Math.pow(y2 - y1, 2) + 
                Math.pow(z2 - z1, 2)
            );
            
            player.sendMessage(ChatColor.GREEN + "Distanz: " +
                              ChatColor.GOLD + String.format("%.2f", distance) + " blocks");
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Ungültige Koordinaten");
        }
        return true;
    }

    private boolean handleStackCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /stack <toStacks|toItems> <number> [stackSize]");
            player.sendMessage(ChatColor.YELLOW + "Or: /stack toItems <stacks> <remainder> [stackSize]");
            return true;
        }

        try {
            String mode = args[0].toLowerCase();
            
            if (mode.equals("tostacks")) {
                // toStacks mode: convert items to stacks
                if (args.length < 2 || args.length > 3) {
                    player.sendMessage(ChatColor.YELLOW + "Usage: /stack toStacks <items> [stackSize]");
                    return true;
                }
                
                int number = Integer.parseInt(args[1]);
                int stackSize = args.length == 3 ? Integer.parseInt(args[2]) : 64; // Default stack size is 64
                
                if (stackSize <= 0) {
                    player.sendMessage(ChatColor.RED + "Stack size must be positive");
                    return true;
                }

                // Convert items to stacks
                int stacks = number / stackSize;
                int rest = number % stackSize;
                player.sendMessage(ChatColor.GREEN + String.valueOf(number) + " items = " + 
                                  ChatColor.GOLD + String.valueOf(stacks) + " stacks" + 
                                  ChatColor.YELLOW + " and " + String.valueOf(rest) + " items" +
                                  (stackSize != 64 ? ChatColor.GRAY + " (Stack size: " + String.valueOf(stackSize) + ")" : ""));
            } 
            else if (mode.equals("toitems")) {
                // toItems mode: convert stacks to items
                if (args.length == 2 || (args.length == 3 && !isInteger(args[2]))) {
                    // Format: /stack toItems <stacks> [stackSize]
                    int stacks = Integer.parseInt(args[1]);
                    int stackSize = args.length == 3 ? Integer.parseInt(args[2]) : 64;
                    
                    if (stackSize <= 0) {
                        player.sendMessage(ChatColor.RED + "Stack size must be positive");
                        return true;
                    }
                    
                    int items = stacks * stackSize;
                    player.sendMessage(ChatColor.GREEN + String.valueOf(stacks) + " stacks = " + 
                                      ChatColor.GOLD + String.valueOf(items) + " items" +
                                      (stackSize != 64 ? ChatColor.GRAY + " (Stack size: " + String.valueOf(stackSize) + ")" : ""));
                } 
                else if (args.length >= 3) {
                    // Format: /stack toItems <stacks> <remainder> [stackSize]
                    int stacks = Integer.parseInt(args[1]);
                    int remainder = Integer.parseInt(args[2]);
                    int stackSize = args.length == 4 ? Integer.parseInt(args[3]) : 64;
                    
                    if (stackSize <= 0) {
                        player.sendMessage(ChatColor.RED + "Stack size must be positive");
                        return true;
                    }
                    
                    if (remainder >= stackSize) {
                        player.sendMessage(ChatColor.RED + "Remainder should be less than stack size (" + String.valueOf(stackSize) + ")");
                        return true;
                    }
                    
                    int items = (stacks * stackSize) + remainder;
                    player.sendMessage(ChatColor.GREEN + String.valueOf(stacks) + " stacks and " + String.valueOf(remainder) + " items = " + 
                                      ChatColor.GOLD + String.valueOf(items) + " total items" +
                                      (stackSize != 64 ? ChatColor.GRAY + " (Stack size: " + String.valueOf(stackSize) + ")" : ""));
                }
                else {
                    player.sendMessage(ChatColor.YELLOW + "Usage: /stack toItems <stacks> [remainder] [stackSize]");
                    return true;
                }
            }
            else {
                player.sendMessage(ChatColor.RED + "Invalid mode. Use 'toStacks' or 'toItems'");
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid number format");
        }
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (command.getName().equalsIgnoreCase("coord")) {
            if (args.length == 1) {
                // First argument for coord command should be dimension
                List<String> dimensions = Arrays.asList("nether", "overworld");
                return filterCompletions(dimensions, args[0]);
            } else if (args.length >= 2 && args.length <= 4) {
                // For coordinate arguments, suggest current player position
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    int index = args.length - 2; // 0 for x, 1 for y, 2 for z
                    
                    if (index == 0) {
                        completions.add(String.valueOf((int) player.getLocation().getX()));
                    } else if (index == 1) {
                        completions.add(String.valueOf((int) player.getLocation().getY()));
                    } else if (index == 2) {
                        completions.add(String.valueOf((int) player.getLocation().getZ()));
                    }
                }
            }
        } else if (command.getName().equalsIgnoreCase("distance")) {
            if (args.length >= 1 && args.length <= 6) {
                // For coordinate arguments, suggest current player position for first set
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    int index = (args.length - 1) % 3; // 0 for x, 1 for y, 2 for z
                    
                    if (index == 0) {
                        completions.add(String.valueOf((int) player.getLocation().getX()));
                    } else if (index == 1) {
                        completions.add(String.valueOf((int) player.getLocation().getY()));
                    } else if (index == 2) {
                        completions.add(String.valueOf((int) player.getLocation().getZ()));
                    }
                }
            }
        } else if (command.getName().equalsIgnoreCase("stack")) {
            if (args.length == 1) {
                // First argument should be the conversion mode
                return filterCompletions(Arrays.asList("toStacks", "toItems"), args[0]);
            } else if (args.length == 2) {
                // Second argument is the number
                if (args[0].equalsIgnoreCase("toStacks")) {
                    // Suggest common item amounts
                    return filterCompletions(Arrays.asList("64", "128", "192", "256", "512", "1024"), args[1]);
                } else if (args[0].equalsIgnoreCase("toItems")) {
                    // Suggest common stack amounts
                    return filterCompletions(Arrays.asList("1", "2", "4", "8", "16", "32"), args[1]);
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("toItems")) {
                    // Third argument could be remainder or stack size
                    return filterCompletions(Arrays.asList("0", "1", "16", "32", "48", "63", "16", "64"), args[2]);
                } else if (args[0].equalsIgnoreCase("toStacks")) {
                    // Third argument is the optional stack size
                    return filterCompletions(Arrays.asList("16", "64"), args[2]);
                }
            } else if (args.length == 4 && args[0].equalsIgnoreCase("toItems")) {
                // Fourth argument is the stack size when using remainder
                return filterCompletions(Arrays.asList("16", "64"), args[3]);
            }
        } else if (command.getName().equalsIgnoreCase("calc")) {
            if (args.length == 1) {
                // Suggest common mathematical operations
                completions.addAll(Arrays.asList("1+1", "2*2", "10/2", "sqrt(", "sin(", "cos(", "tan(", "log("));
                return filterCompletions(completions, args[0]);
            }
        }
        
        return completions;
    }
    
    private List<String> filterCompletions(List<String> completions, String input) {
        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Helper method to check if a string is an integer
    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
