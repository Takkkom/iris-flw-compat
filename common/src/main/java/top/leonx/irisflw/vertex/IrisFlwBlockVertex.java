package top.leonx.irisflw.vertex;

import com.jozufozu.flywheel.backend.gl.GlNumericType;
import com.jozufozu.flywheel.core.layout.BufferLayout;
import com.jozufozu.flywheel.core.layout.CommonItems;
import com.jozufozu.flywheel.core.layout.PrimitiveItem;
import com.jozufozu.flywheel.core.vertex.BlockVertex;
import com.jozufozu.flywheel.core.vertex.BlockVertexListUnsafe;
import com.jozufozu.flywheel.core.vertex.BlockWriterUnsafe;
import org.jetbrains.annotations.NotNull;
import top.leonx.irisflw.IrisFlw;

import java.nio.ByteBuffer;

public class IrisFlwBlockVertex extends BlockVertex {
    public static final BufferLayout FORMAT = BufferLayout.builder()
            .addItems(CommonItems.VEC3,
                    CommonItems.RGBA,
                    CommonItems.UV,
                    CommonItems.LIGHT_SHORT,
                    CommonItems.NORMAL,
                    CommonItems.PADDING_BYTE)
            .build();


    // Same as IrisVertexFormats.TERRAIN
    public static final BufferLayout IRIS_FORMAT = BufferLayout.builder()
            .addItems(CommonItems.VEC3,         //POSITION  3xFLOAT = 12
                    CommonItems.RGBA,           //COLOR     4xBYTE  = 4
                    CommonItems.UV,             //UV        2xFLOAT = 8
                    CommonItems.LIGHT_SHORT,    //UV2 - LIGHT_SHORT 2xSHORT = 4
                    CommonItems.NORMAL,         //NORMAL    3xBYTE  = 3
                    CommonItems.PADDING_BYTE,   //PADDING   1xBYTE  = 1     ^^^DEFAULT-VERTEX-FORMAT 32 BYTES
                    CommonItems.LIGHT_SHORT,    //ENTITY_ELEMENT
                    CommonItems.VEC4,           //MID_TEXTURE_ELEMENT 4xFLOAT = 16
                    CommonItems.RGBA,            //TANGENT_ELEMENT   4xBYTE  = 4
                    CommonItems.RGB,            //MID_BLOCK_ELEMENT  3xBYTE  = 3
                    CommonItems.PADDING_BYTE    //PADDING_SHORT 1xBYTE  = 1
                    // Total: 52
            )
            .build();

    public static final PrimitiveItem SHORT2 = new PrimitiveItem(GlNumericType.SHORT, 2);

    public static final BufferLayout EXTEND_FORMAT = BufferLayout.builder()
            .addItems(CommonItems.VEC3,         //POSITION  3xFLOAT = 12
                    CommonItems.RGBA,           //COLOR     4xBYTE  = 4
                    CommonItems.UV,             //UV        2xFLOAT = 8
                    CommonItems.LIGHT_SHORT,    //UV2 - LIGHT_SHORT 2xSHORT = 4
                    CommonItems.NORMAL,         //NORMAL    3xBYTE  = 3
                    CommonItems.PADDING_BYTE,   //PADDING   1xBYTE  = 1     ^^^DEFAULT-VERTEX-FORMAT 32 BYTES
                    CommonItems.VEC4,           //EXTEND_DATA 4xFLOAT = 16  xy:midTexCoord, z:tangent w:midBlock
                    SHORT2                      //MC_ENTITY 2xSHORT = 4
                    // Total: 52
            )
            .build();


    @Override
    public @NotNull BufferLayout getLayout() {
        if (IrisFlw.isUsingExtendedVertexFormat()) {
            return EXTEND_FORMAT;
        }
        return super.getLayout();
    }

    @Override
    public @NotNull BlockWriterUnsafe createWriter(ByteBuffer buffer) {
        if (IrisFlw.isUsingExtendedVertexFormat()) {
            return new ExtendedBlockWriterUnsafe(this, buffer);
        }
        return super.createWriter(buffer);
    }

    @Override
    public @NotNull BlockVertexListUnsafe createReader(ByteBuffer buffer, int vertexCount) {
        if (IrisFlw.isUsingExtendedVertexFormat()) {
            return new IrisBlockVertexListUnsafe(buffer, vertexCount);
        }
        return super.createReader(buffer, vertexCount);
    }

    public BlockVertexListUnsafe.@NotNull Shaded createReader(ByteBuffer buffer, int vertexCount, int unshadedStartVertex) {
        if (IrisFlw.isUsingExtendedVertexFormat()) {
            return new IrisBlockVertexListUnsafe.Shaded(buffer, vertexCount, unshadedStartVertex);
        }
        return super.createReader(buffer, vertexCount, unshadedStartVertex);
    }

    @Override
    public @NotNull String getShaderHeader() {
        if (IrisFlw.isUsingExtendedVertexFormat()) {
            return """
                    layout (location = 0) in vec3 _flw_v_pos;
                    layout (location = 1) in vec4 _flw_v_color;
                    layout (location = 2) in vec2 _flw_v_texCoords;
                    layout (location = 3) in vec2 _flw_v_light;
                    layout (location = 4) in vec3 _flw_v_normal;
                    layout (location = 5) in vec4 _flw_v_packed_extended;  // x:midTexCoord, z:tangent, w:midBlock
                    layout (location = 6) in vec2 _flw_v_mc_Entity;        // x:midTexCoord, z:tangent, w:midBlock

                    Vertex FLWCreateVertex() {
                    	Vertex v;
                    	v.pos = _flw_v_pos;
                    	v.color = _flw_v_color;
                    	v.texCoords = _flw_v_texCoords;
                    	v.light = _flw_v_light;
                    	v.normal = _flw_v_normal;
                    	return v;
                    }
                    """;
        }
        return """
                layout (location = 0) in vec3 _flw_v_pos;
                layout (location = 1) in vec4 _flw_v_color;
                layout (location = 2) in vec2 _flw_v_texCoords;
                layout (location = 3) in vec2 _flw_v_light;
                layout (location = 4) in vec3 _flw_v_normal;

                Vertex FLWCreateVertex() {
                	Vertex v;
                	v.pos = _flw_v_pos;
                	v.color = _flw_v_color;
                	v.texCoords = _flw_v_texCoords;
                	v.light = _flw_v_light;
                	v.normal = _flw_v_normal;
                	return v;
                }
                """;
    }
}
