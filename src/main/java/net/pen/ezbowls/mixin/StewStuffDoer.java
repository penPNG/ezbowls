package net.pen.ezbowls.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.StewItem;
//import net.minecraft.text.Text;
import net.minecraft.world.World;
//import net.pen.ezbowls.ezBowls;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(StewItem.class)
public class StewStuffDoer extends Item {

    public StewStuffDoer(Settings settings) {
        super(settings);
    }

    // Had to copy some code from the mc source because the genius devs made this ONE method private
    public boolean canStackAddMore(ItemStack existingStack, ItemStack stack) {
        return !existingStack.isEmpty() && ItemStack.canCombine(existingStack, stack) && existingStack.isStackable() && existingStack.getCount() < existingStack.getMaxCount() && existingStack.getCount() < 64;
    }

    // made finding a bowl easy
    public int getValidSlot(PlayerInventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.main.size(); ++i) {
            if (!canStackAddMore(inventory.main.get(i), stack)) continue;
            return i;
        }
        return -1;
    }

    // puts a bowl in one of a few predetermined empty slots or the last empty one
    public int getLastEmptySlot(PlayerInventory inventory) {
        // technically the other condition isn't necessary because of how eating food and the isEmpty() method works
        if(inventory.getStack(8).isEmpty() || inventory.selectedSlot == 8) return 8; // returns even if the current selected stew/soup is in slot 8
        if(inventory.getStack(17).isEmpty()) return 17; // end of the top row in the inventory

        // finds an empty slot starting from the end of the inventory
        for(int i = 35; i>=0; i--) {
            if(inventory.getStack(i).isEmpty() && i != inventory.selectedSlot) return i;
        }

        // if this returns, the game WILL crash
        return -1;
    }

    /**
     * @author pen
     * @reason i want stews to do this instead of that!
     */
    @Overwrite
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

        // old pieces of code that are kinda neat
        //if(!world.isClient) user.sendMessage(Text.literal(user.getName().getString() + " ate a stew!"));
        // if(!world.isClient) user.damage(DamageSource.FALL, 19);

        ItemStack itemStack = super.finishUsing(stack, world, user);

        // quickly end if player is in creative mode, saves like 3 nanoseconds probably
        if(((PlayerEntity)user).getAbilities().creativeMode)
            return itemStack;

        PlayerInventory inventory = ((PlayerEntity)user).getInventory();
        ItemStack bowl = new ItemStack(Items.BOWL);

        // find some available slots to put a bowl in
        int availableBowlSlot = (getValidSlot(inventory, bowl));
        int availableEmptySlot = (getLastEmptySlot(inventory));

        // There's a specific clause that doesn't work, so there are three cases
        if(availableBowlSlot != -1) {
            inventory.getStack(availableBowlSlot).increment(1);
            return ItemStack.EMPTY;
        } else if(inventory.selectedSlot!=8 && availableEmptySlot != -1){ // i hate this bug, kinda doesn't make sense
            // availableEmptySlot should see that slot 8 is already full of the stew/soup, but it doesn't
            inventory.setStack(availableEmptySlot, new ItemStack(Items.BOWL));
            return ItemStack.EMPTY;
        }

        // This only occurs if the player has the bowl in the last item slot because of how the method works
        return new ItemStack(Items.BOWL);
    }

}
