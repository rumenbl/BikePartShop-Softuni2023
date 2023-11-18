package me.rumenblajev.bikepartshop.web;

import jakarta.servlet.http.Part;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.dto.PartCreateDTO;
import me.rumenblajev.bikepartshop.models.view.OrderViewModel;
import me.rumenblajev.bikepartshop.models.view.PartViewModel;
import me.rumenblajev.bikepartshop.models.view.UserViewModel;
import me.rumenblajev.bikepartshop.services.BikePartService;
import me.rumenblajev.bikepartshop.services.OrderService;
import me.rumenblajev.bikepartshop.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/administration")
public class AdministrationController {
    private final UserService userService;
    private final BikePartService bikePartService;
    private final OrderService orderService;

    @GetMapping("/part/edit/{id}")
    public String editPart(@PathVariable Long id, Model model) {
        PartViewModel partViewModel = bikePartService.findPartViewModelById(id);
        model.addAttribute("partViewModel", partViewModel);
        return "edit-bike-part";
    }

    @PostMapping("/part/edit/{id}")
    public String editPartSubmit(@Valid PartViewModel partViewModel,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 @PathVariable Long id) {
        if(bikePartService.findById(id) == null) {
            return "redirect:/parts/all";
        }
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("partViewModel", partViewModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.partViewModel", bindingResult);
            return "redirect:/administration/part/edit/" + id;
        }
        bikePartService.saveEditedPart(partViewModel, id);
        return "redirect:/parts/all";
    }

    @GetMapping("/part/delete/{id}")
    public String deletePart(@PathVariable Long id) {
        bikePartService.deletePart(id);
        return "redirect:/parts/all";
    }

    @GetMapping("/part/create")
    public String createPart() {
        return "add-bike-part";
    }

    @PostMapping("/part/create")
    public String createPartSubmit(@Valid PartCreateDTO partCreateDTO,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("partCreateDTO", partCreateDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.partCreateDTO", bindingResult);
            return "redirect:/administration/part/create";
        }

        if(bikePartService.checkIfPartTitleAlreadyExists(partCreateDTO.getTitle().trim())) {
            redirectAttributes.addFlashAttribute("partCreateDTO", partCreateDTO);
            redirectAttributes.addFlashAttribute("partAlreadyExists", true);
            return "redirect:/administration/part/create";
        }
        bikePartService.savePart(partCreateDTO);
        return "redirect:/parts/all";
    }

    @GetMapping("/orders/all")
    public String allOrders(Model model) {
        model.addAttribute("allOrders", orderService.findAllOrders());
        return "orders";
    }

    @GetMapping("/users/all")
    public String allUsers(Model model) {
        model.addAttribute("allUsers", userService.findAllUsers());
        return "users";
    }
    @ModelAttribute("allUsers")
    public UserViewModel allUsers() { return new UserViewModel(); }

    @ModelAttribute("allOrders")
    public OrderViewModel allOrders() {
        return new OrderViewModel();
    }

    @ModelAttribute("partViewModel")
    public PartViewModel partViewModel() {
        return new PartViewModel();
    }

    @ModelAttribute("partCreateDTO")
    public PartCreateDTO partCreateDTO() {
        return new PartCreateDTO();
    }

    @ModelAttribute("partAlreadyExists")
    public boolean partAlreadyExists() {
        return false;
    }
}
