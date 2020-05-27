package com.example.expensify.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.expensify.R
import com.example.expensify.databinding.DashboardFragmentBinding

class DashboardFragment : Fragment() {

    private val viewModel by viewModels<DashboardViewModel>()
    private lateinit var binding: DashboardFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dashboard_fragment, container, false)

        binding.addExpenseFab.setOnClickListener { findNavController().navigate(R.id.action_dashboardFragment_to_addExpenseFragment) }

        binding.incomeAmountText.text = viewModel.income
        binding.expensesAmountText.text = viewModel.expenses
        binding.balanceAmountText.text = viewModel.balance

        return binding.root
    }

}
