package net.ramixin.bbg;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface BeamBeGone {

    TagKey<Block> MAKES_BEAM_INVISIBLE = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("bbg","makes_beam_invisible"));
}