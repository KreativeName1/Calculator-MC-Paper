package org.KreativeName.calculator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DistanceCommand extends Command {
    public DistanceCommand() {
        super("dist", "Calculates the distance between two 3D pointss", "/distance <x1> <y1> <z1> <x2> <y2> <z2>", List.of(""));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String @NotNull [] args) {
        Player player = (Player) commandSender;
        if (args.length != 6) {
            player.sendMessage(ChatColor.YELLOW + "Usage: " + usageMessage);
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

            player.sendMessage(ChatColor.GREEN + "Distance: " +
                    ChatColor.GOLD + String.format("%.2f", distance) + " blocks");
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
        if (args.length >= 1 && args.length <= 6) {
            if (sender instanceof Player player) {
                int index = (args.length - 1) % 3;

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
