package net.pen.ezbowls.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.StewItem;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.pen.ezbowls.ezBowls;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(StewItem.class)
public class StewStuffDoer extends Item {

    public StewStuffDoer(Settings settings) {
        super(settings);
    }

    // TODO make finding a bowl easy
    public int getSlot(PlayerInventory inventory, Item item) {
        return 0;
    }

    // TODO Make sure ItemStack in slot has less than 64 items
    public boolean validSlot(int slot) {
        return false;
    }

    /**
     * @author pen
     * @reason i want stews to do this instead of that!
     */
    @Overwrite
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if(!world.isClient) user.sendMessage(Text.literal(user.getName().getString() + " ate a stew!"));
        // if(!world.isClient) user.damage(DamageSource.FALL, 19);

        ItemStack itemStack = super.finishUsing(stack, world, user);

        // TODO Use getSlot to find bowl and add 1 to it if validSlot returns true
        PlayerInventory inventory = ((PlayerEntity)user).getInventory();
        ItemStack[] leaves = new ItemStack[41];
        if(!world.isClient) {
            for (int i = 0; i < 41; i++) {
                leaves[i] = new ItemStack(Items.ACACIA_LEAVES);
                leaves[i].setCount(i+1);
                inventory.setStack(i, leaves[i]);
            }
        }
        ezBowls.LOGGER.info(String.valueOf(inventory.isEmpty()));

        ezBowls.LOGGER.info(String.valueOf(Text.literal(inventory.toString())));

        if(((PlayerEntity)user).getAbilities().creativeMode)
            return itemStack;

        // TODO remove ItemStack if a slot already containing bowls exists and isn't full
        return new ItemStack(Items.ITEM_FRAME);
    }

}
