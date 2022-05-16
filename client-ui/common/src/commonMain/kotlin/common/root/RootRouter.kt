package root
//
//import com.arkivanov.decompose.ComponentContext
//import com.arkivanov.decompose.router.RouterState
//import com.arkivanov.decompose.router.activeChild
//import com.arkivanov.decompose.router.popWhile
//import com.arkivanov.decompose.router.router
//import com.arkivanov.decompose.value.Value
//import com.arkivanov.essenty.parcelable.Parcelable;
//import com.arkivanov.essenty.parcelable.Parcelize;
//import com.badoo.reaktive.observable.Observable;
//import Root.Child.OverviewChild
//
//class RootRouter(
//    componentContext: ComponentContext,
//) {
//    private val router =
//        componentContext.router<Config, DetailsChild>(
//            initialConfiguration = Config.None,
//            key = "DetailsRouter",
//            childFactory = ::createChild
//        )
//
//    val state: Value<RouterState<Config, DetailsChild>> = router.state
//
//    private fun createChild(config: Config, componentContext: ComponentContext): DetailsChild =
//        when (config) {
////            is Config.
////            Details -> DetailsChild.Details(
////                EntityDetails(
////                    componentContext = componentContext,
////                    entityId = config.EntityId
////                )
////            )
//            Config.OverviewUi -> OverviewChild()
//            Config.Registration -> TODO()
//            Config.Map -> TODO()
//        }
//
//    private fun EntityDetails(componentContext: ComponentContext, entityId: Long): EntityDetails =
//        EntityDetailsComponent(
//            componentContext = componentContext,
//            entityProvider = entityProvider,
//            entityId = entityId,
//            isToolbarVisible = isToolbarVisible,
//            onComplete = onFinished
//        )
//
//    fun showEntity(id: Long) {
//        router.navigate(transformer = {stack ->
//            stack
//                .dropLastWhile {
//                    it is Config.Details
//                }
//                .plus(Config.Details(EntityId = id))
//        }, onComplete = { _, _ -> })
//    }
//
//    fun closeEntity() {
//        router.popWhile {
//            it !is Config.None
//        }
//    }
//
//    /**
//     * Configuration routes
//     */
//    sealed class Config : Parcelable {
//        @Parcelize
//        object OverviewUi() : Config()
//        @Parcelize
//        object Registration() : Config()
//        @Parcelize
//        object Map() : Config()
//    }
//}