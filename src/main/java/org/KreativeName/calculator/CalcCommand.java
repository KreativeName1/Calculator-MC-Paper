package org.KreativeName.calculator;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalcCommand extends Command {
    public CalcCommand() {
        super("calc", "A calculator", "/calc <expression>", List.of(""));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String @NotNull [] args) {
        Player player = (Player) commandSender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Usage: " + usageMessage);
            return true;
        }

        String input = String.join(" ", args);
        try {
            Expression expression = new ExpressionBuilder(input).build();
            double result = expression.evaluate();
            player.sendMessage(ChatColor.GREEN + "Result: " + result);
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Invalid input.");
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(
            @NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args)
            throws IllegalArgumentException {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("1+1", "2*2", "10/2", "sqrt(", "sin(", "cos(", "tan(", "log("));
            return CommandHelper.FilterCompletions(completions, args[0]);
        }
        return completions;
    }
}
