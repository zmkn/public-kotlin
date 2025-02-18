package enumeration

enum class AliLlmImageModel(
    val code: String,
    val types: List<AliLlmModelType>,
    val outputType: AliLlmModelOutputType,
    val title: String,
    val free: Boolean,
) {
    WanxV1(
        "wanx-v1",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Image,
        "通义万相-文本生成图像",
        false,
    ),
    WanxSketch(
        "wanx-sketch-to-image-lite",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Image,
        "通义万相-涂鸦作画",
        false,
    ),
    WanxXPainting(
        "wanx-x-painting",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Image,
        "通义万相-图像局部重绘",
        false,
    ),
    WanxStyleRepaintV1(
        "wanx-style-repaint-v1",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Image,
        "通义万相-人像风格重绘",
        false,
    ),
    WanxBackgroundGenerationV2(
        "wanx-background-generation-v2",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Image,
        "图像背景生成",
        false,
    ),
    ImageOutPainting(
        "image-out-painting",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Image,
        "图像画面扩展",
        false,
    ),
    ImageInstanceSegmentation(
        "image-instance-segmentation",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Image,
        "人物实例分割",
        false,
    ),
    ImageEraseCompletion(
        "image-erase-completion",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Image,
        "图像擦除补全",
        false,
    ),
    WanxStyleCosplayV1(
        "wanx-style-cosplay-v1",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Image,
        "Cosplay动漫人物生成",
        false,
    ),
    VirtualmodelV2(
        "virtualmodel-v2",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Image,
        "虚拟模特",
        true,
    ),
    ShoemodelV1(
        "shoemodel-v1",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Image,
        "鞋靴模特",
        true,
    ),
    WanxPosterGenerationV1(
        "wanx-poster-generation-v1",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Image,
        "创意海报生成",
        true,
    ),
    WanxAst(
        "wanx-poster-generation-v1",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Image,
        "图配文",
        true,
    ),
    Aitryon(
        "aitryon",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Image,
        "AI试衣",
        false,
    ),
    AitryonRefiner(
        "aitryon-refiner",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Image,
        "AI试衣-图片精修",
        false,
    ),
    ;

    companion object {
        fun byCode(code: String): AliLlmImageModel {
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
                else -> throw IllegalArgumentException("AliLlmModel code [$code] not recognized.")
            }
        }
    }
}
