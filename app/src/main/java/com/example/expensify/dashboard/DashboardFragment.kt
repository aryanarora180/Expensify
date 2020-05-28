package com.example.expensify.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensify.R
import com.example.expensify.databinding.DashboardFragmentBinding

class DashboardFragment : Fragment() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var binding: DashboardFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dashboard_fragment, container, false)
        viewModel =
            ViewModelProvider(this, DashboardViewModelFactory(requireActivity().application)).get(
                DashboardViewModel::class.java
            )

        binding.addExpenseFab.setOnClickListener { findNavController().navigate(R.id.action_dashboardFragment_to_addExpenseFragment) }

        viewModel.getExpenses().observe(viewLifecycleOwner, Observer { list ->
            binding.expensesRecycler.apply {
                if (list.isNullOrEmpty()) {
                    binding.noExpensesText.visibility = View.VISIBLE
                    binding.expensesRecycler.visibility = View.GONE
                } else {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = ExpenseAdapter(list, findNavController())
                }
            }
        })

        viewModel.totalBalance.observe(viewLifecycleOwner, Observer { balance ->
            binding.balanceAmountText.text = viewModel.formatAmount(balance)
        })

        viewModel.totalIncome.observe(viewLifecycleOwner, Observer { income ->
            binding.incomeAmountText.text = viewModel.formatAmount(income)
        })

        viewModel.totalExpenses.observe(viewLifecycleOwner, Observer { expenses ->
            binding.expensesAmountText.text = viewModel.formatAmount(expenses)
        })

        return binding.root
    }

}
