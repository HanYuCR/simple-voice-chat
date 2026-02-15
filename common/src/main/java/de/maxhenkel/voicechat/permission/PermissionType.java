package de.maxhenkel.voicechat.permission;

import net.minecraft.server.level.ServerPlayer;
import javax.annotation.Nullable;

public enum PermissionType {

    EVERYONE, NOONE, OPS;

    boolean hasPermission(@Nullable ServerPlayer player) {
        if (player == null) {
            return this == EVERYONE;
        }
        
        return switch (this) {
            case EVERYONE -> true;
            case NOONE -> false;
            case OPS -> player.createCommandSourceStack().hasPermission(player.server.getOperatorUserPermissionLevel());
        };
    }

}
