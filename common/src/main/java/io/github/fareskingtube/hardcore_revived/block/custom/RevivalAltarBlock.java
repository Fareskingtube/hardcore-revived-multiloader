package io.github.fareskingtube.hardcore_revived.block.custom;

import com.mojang.serialization.MapCodec;
import io.github.fareskingtube.hardcore_revived.block.entity.TickableBlockEntity;
import io.github.fareskingtube.hardcore_revived.block.entity.custom.RevivalAltarBlockEntity;
import io.github.fareskingtube.hardcore_revived.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RevivalAltarBlock extends BaseEntityBlock implements EntityBlock {
    //    Yes. I know this is horrible code, but I can't find a better way to do it ¯\_(ツ)_/¯
    private static final VoxelShape ALTAR_BASE = Shapes.join(
            Block.box(1, 0, 1, 15, 1, 15),
            Block.box(2, 1, 2, 14, 2, 14),
            BooleanOp.OR
    );

    private static final VoxelShape ALTAR_MIDDLE = Shapes.join(ALTAR_BASE,
            Block.box(4, 2, 4, 12, 10, 12),
            BooleanOp.OR);

    private static final VoxelShape ALTAR_TOP = Shapes.join(ALTAR_MIDDLE,
            Block.box(1, 10, 1, 15, 12, 15),
            BooleanOp.OR);

    private static final VoxelShape ALTAR_PLATES = Shapes.join(
            Block.box(3, 12, 3, 13, 12.75, 13),
            Block.box(5, 12.75, 5, 11, 13.25, 11),
            BooleanOp.OR
    );

    private static final VoxelShape ALTAR_FULL_TOP = Shapes.join(
            ALTAR_TOP,
            ALTAR_PLATES,
            BooleanOp.OR
    );


    private static final VoxelShape CORNER_NW = Block.box(0, 0, 0, 2, 13, 2);
    private static final VoxelShape CORNER_NE = Block.box(14, 0, 0, 16, 13, 2);
    private static final VoxelShape CORNER_SW = Block.box(14, 0, 14, 16, 13, 16);
    private static final VoxelShape CORNER_SE = Block.box(0, 0, 14, 2, 13, 16);

    private static final VoxelShape ALTAR_CORNERS = Shapes.or(CORNER_NE, CORNER_NW, CORNER_SW, CORNER_SE);
    private static final VoxelShape ALTAR_CORNERS_HOLLOW = Shapes.join(ALTAR_CORNERS,
            Block.box(1, 10, 1, 15, 13, 15),
            BooleanOp.ONLY_FIRST);

    public static final VoxelShape ALTAR_FINAL = Shapes.join(ALTAR_FULL_TOP, ALTAR_CORNERS_HOLLOW, BooleanOp.OR);

    public static final MapCodec<RevivalAltarBlock> CODEC = RevivalAltarBlock.simpleCodec(RevivalAltarBlock::new);
//    private static final Logger log = LoggerFactory.getLogger(RevivalAltarBlock.class);

    public RevivalAltarBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RevivalAltarBlockEntity(pos, state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return ALTAR_FINAL;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof RevivalAltarBlockEntity revivalAltarBlockEntity) {
                Containers.dropContents(world, pos, (revivalAltarBlockEntity));
                world.updateNeighbourForOutputSignal(pos, this);
                revivalAltarBlockEntity.removeQueuedPlayer();
            }
            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof RevivalAltarBlockEntity revivalAltarBlockEntity) {
            revivalAltarBlockEntity.isMultiblock(world, revivalAltarBlockEntity.getBlockPos());
            if (hand.equals(InteractionHand.OFF_HAND)) return ItemInteractionResult.FAIL;
            if (revivalAltarBlockEntity.isEmpty() && stack.is(ModItems.HARDCORE_HEART)) {
                if (!world.isClientSide()) {
                    revivalAltarBlockEntity.setItem(0, stack.copyWithCount(1));
                    world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
                    stack.shrink(1);

                    revivalAltarBlockEntity.setChanged();
                    revivalAltarBlockEntity.queuePlayerRevival();
                }

                return ItemInteractionResult.SUCCESS;
            } else if (!revivalAltarBlockEntity.isEmpty() && stack.isEmpty() && !player.isShiftKeyDown()) {
                if (!world.isClientSide()) {
                    ItemStack stackOnRevivalAltar = revivalAltarBlockEntity.getItem(0);
                    player.setItemInHand(hand, stackOnRevivalAltar);
                    world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                    revivalAltarBlockEntity.setItem(0, ItemStack.EMPTY);

                    revivalAltarBlockEntity.setChanged();
                    revivalAltarBlockEntity.removeQueuedPlayer();
                }

                return ItemInteractionResult.SUCCESS;
            } else {
                return ItemInteractionResult.FAIL;
            }
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return TickableBlockEntity.getTicker();
    }
}

