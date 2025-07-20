package net.ramixin.bbg;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public interface BeamBeGone {

    TagKey<Block> MAKES_BEAM_INVISIBLE = TagKey.of(Registries.BLOCK.getKey(), Identifier.of("bbg","makes_beam_invisible"));
}