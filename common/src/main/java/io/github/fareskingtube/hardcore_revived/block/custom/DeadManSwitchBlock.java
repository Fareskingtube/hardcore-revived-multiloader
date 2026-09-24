package io.github.fareskingtube.hardcore_revived.block.custom;

import com.mojang.serialization.MapCodec;
import io.github.fareskingtube.hardcore_revived.block.entity.custom.DeadManSwitchBlockEntity;
import io.github.fareskingtube.hardcore_revived.component.ModDataComponentTypes;
import io.github.fareskingtube.hardcore_revived.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DeadManSwitchBlock extends BaseEntityBlock implements EntityBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 1, 16);


    public static final MapCodec<DeadManSwitchBlock> CODEC = DeadManSwitchBlock.simpleCodec(DeadManSwitchBlock::new);

    public DeadManSwitchBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any()
                .setValue(POWERED, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DeadManSwitchBlockEntity(blockPos, blockState);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DeadManSwitchBlockEntity deadManSwitchBlockEntity) {
                Containers.dropContents(world, pos, (deadManSwitchBlockEntity));
                world.updateNeighbourForOutputSignal(pos, this);
                deadManSwitchBlockEntity.removeQueuedPlayer();
            }
            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        builder.add(FACING);
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    public void pulse(BlockState state, Level level, BlockPos pos) {
        level.setBlock(pos, state.setValue(POWERED, true), Block.UPDATE_ALL);
        level.scheduleTick(pos, this, 30);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED)) {
            level.setBlock(pos, state.setValue(POWERED, false), Block.UPDATE_ALL);
        }
    }


    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof DeadManSwitchBlockEntity deadManSwitchBlockEntity) {
            if (hand.equals(InteractionHand.OFF_HAND)) return ItemInteractionResult.FAIL;
            if (deadManSwitchBlockEntity.isEmpty() && stack.is(ModItems.BINDING_TABLET)) {
                if (!world.isClientSide()) {
                    deadManSwitchBlockEntity.setItem(0, stack.copyWithCount(1));
                    if (stack.has(ModDataComponentTypes.SELECTED_PLAYER)) {
                        deadManSwitchBlockEntity.queuePlayerDeath();
                    }
                    world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
                    stack.shrink(1);

                    deadManSwitchBlockEntity.setChanged();
                }

                return ItemInteractionResult.SUCCESS;
            } else if (!deadManSwitchBlockEntity.isEmpty() && stack.isEmpty() && !player.isShiftKeyDown()) {
                if (!world.isClientSide()) {
                    ItemStack stackOnDeadManSwitch = deadManSwitchBlockEntity.getItem(0);
                    if (stackOnDeadManSwitch.has(ModDataComponentTypes.SELECTED_PLAYER)) {
                        deadManSwitchBlockEntity.removeQueuedPlayer();
                    }
                    player.setItemInHand(hand, stackOnDeadManSwitch);
                    world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                    deadManSwitchBlockEntity.setItem(0, ItemStack.EMPTY);


                    deadManSwitchBlockEntity.setChanged();
                }

                return ItemInteractionResult.SUCCESS;
            } else {
                return ItemInteractionResult.FAIL;
            }
        }
        return ItemInteractionResult.FAIL;
    }
}
