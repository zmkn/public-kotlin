package enumeration

enum class AliyunLlmImageModel(
    val code: String,
    val types: List<AliyunLlmModelType>,
    val outputType: AliyunLlmModelOutputType,
    val title: String,
    val free: Boolean,
) {
    WanxV1(
        "wanx-v1",
        listOf(AliyunLlmModelType.InputImage),
        AliyunLlmModelOutputType.Image,
        "通义万相-文本生成图像",
        false,
    ),
    WanxSketch(
        "wanx-sketch-to-image-lite",
        listOf(AliyunLlmModelType.InputImage),
        AliyunLlmModelOutputType.Image,
        "通义万相-涂鸦作画",
        false,
    ),
    WanxXPainting(
        "wanx-x-painting",
        listOf(AliyunLlmModelType.InputImage),
        AliyunLlmModelOutputType.Image,
        "通义万相-图像局部重绘",
        false,
    ),
    WanxStyleRepaintV1(
        "wanx-style-repaint-v1",
        listOf(AliyunLlmModelType.InputImage),
        AliyunLlmModelOutputType.Image,
        "通义万相-人像风格重绘",
        false,
    ),
    WanxBackgroundGenerationV2(
        "wanx-background-generation-v2",
        listOf(AliyunLlmModelType.InputImage),
        AliyunLlmModelOutputType.Image,
        "图像背景生成",
        false,
    ),
    ImageOutPainting(
        "image-out-painting",
        listOf(AliyunLlmModelType.InputImage),
        AliyunLlmModelOutputType.Image,
        "图像画面扩展",
        false,
    ),
    ImageInstanceSegmentation(
        "image-instance-segmentation",
        listOf(AliyunLlmModelType.InputImage),
        AliyunLlmModelOutputType.Image,
        "人物实例分割",
        false,
    ),
    ImageEraseCompletion(
        "image-erase-completion",
        listOf(AliyunLlmModelType.InputImage),
        AliyunLlmModelOutputType.Image,
        "图像擦除补全",
        false,
    ),
    WanxStyleCosplayV1(
        "wanx-style-cosplay-v1",
        listOf(AliyunLlmModelType.InputImage),
        AliyunLlmModelOutputType.Image,
        "Cosplay动漫人物生成",
        false,
    ),
    VirtualmodelV2(
        "virtualmodel-v2",
        listOf(AliyunLlmModelType.InputImage),
        AliyunLlmModelOutputType.Image,
        "虚拟模特",
        true,
    ),
    ShoemodelV1(
        "shoemodel-v1",
        listOf(AliyunLlmModelType.InputImage),
        AliyunLlmModelOutputType.Image,
        "鞋靴模特",
        true,
    ),
    WanxPosterGenerationV1(
        "wanx-poster-generation-v1",
        listOf(AliyunLlmModelType.InputImage),
        AliyunLlmModelOutputType.Image,
        "创意海报生成",
        true,
    ),
    WanxAst(
        "wanx-poster-generation-v1",
        listOf(AliyunLlmModelType.InputImage),
        AliyunLlmModelOutputType.Image,
        "图配文",
        true,
    ),
    Aitryon(
        "aitryon",
        listOf(AliyunLlmModelType.InputImage),
        AliyunLlmModelOutputType.Image,
        "AI试衣",
        false,
    ),
    AitryonRefiner(
        "aitryon-refiner",
        listOf(AliyunLlmModelType.InputImage),
        AliyunLlmModelOutputType.Image,
        "AI试衣-图片精修",
        false,
    ),
    ;

    companion object {
        fun byCode(code: String): AliyunLlmImageModel {
            return when (code) {
                WanxV1.code -> WanxV1
                WanxSketch.code -> WanxSketch
                WanxXPainting.code -> WanxXPainting
                WanxStyleRepaintV1.code -> WanxStyleRepaintV1
                WanxBackgroundGenerationV2.code -> WanxBackgroundGenerationV2
                ImageOutPainting.code -> ImageOutPainting
                ImageInstanceSegmentation.code -> ImageInstanceSegmentation
                ImageEraseCompletion.code -> ImageEraseCompletion
                WanxStyleCosplayV1.code -> WanxStyleCosplayV1
                VirtualmodelV2.code -> VirtualmodelV2
                ShoemodelV1.code -> ShoemodelV1
                WanxPosterGenerationV1.code -> WanxPosterGenerationV1
                WanxAst.code -> WanxAst
                Aitryon.code -> Aitryon
                AitryonRefiner.code -> AitryonRefiner
                else -> throw IllegalArgumentException("AliyunLlmModel code [$code] not recognized.")
            }
        }
    }
}
