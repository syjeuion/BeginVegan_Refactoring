//package com.example.presentation.view.test
//
//import com.example.beginvegan.R
//import com.example.beginvegan.config.BaseFragment
//import com.example.beginvegan.databinding.FragmentTestQuestionMilkBinding
//
//class TestQuestionMilkFragment : BaseFragment<FragmentTestQuestionMilkBinding>(
//    FragmentTestQuestionMilkBinding::bind,R.layout.fragment_test_question_milk) {
//    val INDEX = 5
//
//    override fun init() {
//        //progress bar
//        (parentFragment as VeganTestOngoingFragment).setProgressValue(INDEX)
//
//        val testActivity =(activity as VeganTestActivity)
//        val testAfterFragment = VeganTestAfterFragment.newInstance()
//
//        binding.btnAnswerA.setOnClickListener{
//            testActivity.saveUserType(
//                getString(R.string.vegan_type_lacto_kr),
//                getString(R.string.vegan_type_lacto_eng),
//                getString(R.string.test_result_lacto),
//                INDEX
//            )
//            testActivity.changeTestState(testAfterFragment)
//        }
//        binding.btnAnswerB.setOnClickListener{
//            testActivity.saveUserType(
//                getString(R.string.vegan_type_vegan_kr),
//                getString(R.string.vegan_type_vegan_eng),
//                getString(R.string.test_result_vegan),
//                INDEX
//            )
//            testActivity.changeTestState(testAfterFragment)
//        }
//    }
//
//    companion object{
//        fun newInstance(): TestQuestionMilkFragment {
//            return TestQuestionMilkFragment()
//        }
//    }
//
//}