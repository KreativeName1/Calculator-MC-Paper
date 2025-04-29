package org.KreativeName.calculator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StackCommand extends Command {
    public StackCommand() {
        super("stacks", "Converts between stacks and items", "/stack toStacks <items> [stackSize]\n" +
                "/stack toItems <stacks> [stackSize]\n" +
                "/stack toItems <stacks> <remainder> [stackSize]", List.of(""));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String @NotNull [] args) {
        Player player = (Player) commandSender;
        if (args.length < 2) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /stack <toStacks|toItems> <number> [stackSize]");
            player.sendMessage(ChatColor.YELLOW + "Or: /stack toItems <stacks> <remainder> [stackSize]");
            return true;
        }

        try {
            String mode = args[0].toLowerCase();

            if (mode.equals("tostacks")) {
                if (args.length < 2 || args.length > 3) {
                    player.sendMessage(ChatColor.YELLOW + "Usage: /stack toStacks <items> [stackSize]");
                    return true;
                }

                int number = Integer.parseInt(args[1]);
                int stackSize = args.length == 3 ? Integer.parseInt(args[2]) : 64;

                if (stackSize <= 0) {
                    player.sendMessage(ChatColor.RED + "Stack size must be positive");
                    return true;
                }

                int stacks = number / stackSize;
                int rest = number % stackSize;
                player.sendMessage(ChatColor.GREEN + String.valueOf(number) + " items = " +
                        ChatColor.GOLD + stacks + " stacks" +
                        ChatColor.YELLOW + " and " + rest + " items" +
                        (stackSize != 64 ? ChatColor.GRAY + " (Stack size: " + stackSize + ")" : ""));
            } else if (mode.equals("toitems")) {
                if (args.length == 2 || (args.length == 3 && !CommandHelper.IsInteger(args[2]))) {
                    int stacks = Integer.parseInt(args[1]);
                    int stackSize = args.length == 3 ? Integer.parseInt(args[2]) : 64;

                    if (stackSize <= 0) {
                        player.sendMessage(ChatColor.RED + "Stack size must be positive");
                        return true;
                    }

                    int items = stacks * stackSize;
                    player.sendMessage(ChatColor.GREEN + String.valueOf(stacks) + " stacks = " +
                            ChatColor.GOLD + items + " items" +
                            (stackSize != 64 ? ChatColor.GRAY + " (Stack size: " + stackSize + ")" : ""));
                } else if (args.length >= 3) {
                    int stacks = Integer.parseInt(args[1]);
                    int remainder = Integer.parseInt(args[2]);
                    int stackSize = args.length == 4 ? Integer.parseInt(args[3]) : 64;

                    if (stackSize <= 0) {
                        player.sendMessage(ChatColor.RED + "Stack size must be positive");
                        return true;
                    }

                    if (remainder >= stackSize) {
                        player.sendMessage(ChatColor.RED + "Remainder should be less than stack size (" + stackSize + ")");
                        return true;
                    }

                    int items = (stacks * stackSize) + remainder;
                    player.sendMessage(ChatColor.GREEN + String.valueOf(stacks) + " stacks and " + remainder + " items = " +
                            ChatColor.GOLD + items + " total items" +
                            (stackSize != 64 ? ChatColor.GRAY + " (Stack size: " + stackSize + ")" : ""));
                } else {
                    player.sendMessage(ChatColor.YELLOW + "Usage: /stack toItems <stacks> [remainder] [stackSize]");
                    return true;
                }
            } else {
                player.sendMessage(ChatColor.RED + "Invalid mode. Use 'toStacks' or 'toItems'");
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid number format");
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(
            @NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args)
            throws IllegalArgumentException {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            return CommandHelper.FilterCompletions(Arrays.asList("toStacks", "toItems"), args[0]);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("toStacks")) {
                return CommandHelper.FilterCompletions(Arrays.asList("64", "128", "192", "256", "512", "1024"), args[1]);
            } else if (args[0].equalsIgnoreCase("toItems")) {
                return CommandHelper.FilterCompletions(Arrays.asList("1", "2", "4", "8", "16", "32"), args[1]);
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("toItems")) {
                return CommandHelper.FilterCompletions(Arrays.asList("0", "1", "16", "32", "48", "63", "16", "64"), args[2]);
            } else if (args[0].equalsIgnoreCase("toStacks")) {
                return CommandHelper.FilterCompletions(Arrays.asList("16", "64"), args[2]);
            }
        } else if (args.length == 4 && args[0].equalsIgnoreCase("toItems")) {
            return CommandHelper.FilterCompletions(Arrays.asList("16", "64"), args[3]);
        }
        return completions;
    }
}
