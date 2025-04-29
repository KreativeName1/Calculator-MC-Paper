package org.KreativeName.calculator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoordCommand extends Command {

    public CoordCommand() {
        super("coord", "Calculates nether/overworld coordinates", "/coord [toNether|toOverworld] <x> <y> <z>", List.of(""));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String @NotNull [] args) {
        Player player = (Player) commandSender;
        if (args.length != 4) {
            player.sendMessage(ChatColor.YELLOW + "Usage: " + usageMessage);
            return true;
        }

        try {
            String dimension = args[0].toLowerCase();
            double x = Double.parseDouble(args[1]);
            double y = Double.parseDouble(args[2]);
            double z = Double.parseDouble(args[3]);

            if (dimension.equals("tonether")) {
                double netherX = x / 8;
                double netherZ = z / 8;
                player.sendMessage(ChatColor.GREEN + "Nether coordinates: " +
                        ChatColor.GOLD + String.format("%.2f", netherX) + ", " +
                        String.format("%.2f", y) + ", " +
                        String.format("%.2f", netherZ));
            } else if (dimension.equals("tooverworld")) {
                double overworldX = x * 8;
                double overworldZ = z * 8;
                player.sendMessage(ChatColor.GREEN + "Overworld coordinates: " +
                        ChatColor.GOLD + String.format("%.2f", overworldX) + ", " +
                        String.format("%.2f", y) + ", " +
                        String.format("%.2f", overworldZ));
            } else {
                player.sendMessage(ChatColor.RED + "Unknown dimension, please specify 'toNether' or 'toOverworld'.");
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid coordinates");
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(
            @NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args)
            throws IllegalArgumentException {

        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> dimensions = Arrays.asList("toNether", "toOverworld");
            return CommandHelper.FilterCompletions(dimensions, args[0]);
        } else if (args.length >= 2 && args.length <= 4) {
            if (sender instanceof Player player) {
                int index = args.length - 2;
                if (index == 0) {
                    completions.add(String.valueOf((int) player.getLocation().getX()));
                } else if (index == 1) {
                    completions.add(String.valueOf((int) player.getLocation().getY()));
                } else if (index == 2) {
                    completions.add(String.valueOf((int) player.getLocation().getZ()));
                }
            }
        }
        return completions;
    }
}
