package org.KreativeName.calculator;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Calculator extends JavaPlugin {

    private CommandMap commandMap;
    @Override
    public void onEnable() {
        commandMap = getServer().getCommandMap();
        commandMap.register("calculator", new CalcCommand());
        commandMap.register("calculator", new CoordCommand());
        commandMap.register("calculator", new DistanceCommand());
        commandMap.register("calculator", new StackCommand());
    }

}