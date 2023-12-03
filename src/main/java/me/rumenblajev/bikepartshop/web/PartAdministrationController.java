package me.rumenblajev.bikepartshop.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.dto.PartCreateDTO;
import me.rumenblajev.bikepartshop.models.view.PartViewModel;
import me.rumenblajev.bikepartshop.services.BikePartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/administration/parts")
public class PartAdministrationController {

    private final BikePartService bikePartService;

    @GetMapping("/edit/{id}")
    public String editPart(final @PathVariable Long id,
                           final Model model) {

        PartViewModel partViewModel = bikePartService.findPartViewModelById(id);
        model.addAttribute("partViewModel", partViewModel);

        return "edit-bike-part";
    }

    @PostMapping("/edit/{id}")
    public String editPartSubmit(final @Valid PartViewModel partViewModel,
                                 final BindingResult bindingResult,
                                 final RedirectAttributes redirectAttributes,
                                 final @PathVariable Long id) {

        if (bikePartService.findById(id).isEmpty()) {
            return "redirect:/parts/all";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("partViewModel", partViewModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.partViewModel", bindingResult);
            return "redirect:/administration/parts/edit/" + id;
        }

        bikePartService.saveEditedPart(partViewModel, id);

        return "redirect:/parts/all";
    }

    @GetMapping("/delete/{id}")
    public String deletePart(final @PathVariable Long id) {
        bikePartService.deletePart(id);
        return "redirect:/parts/all";
    }

    @GetMapping("/create")
    public String createPart() {
        return "add-bike-part";
    }

    @PostMapping("/create")
    public String createPartSubmit(final @Valid PartCreateDTO partCreateDTO,
                                   final BindingResult bindingResult,
                                   final RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("partCreateDTO", partCreateDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.partCreateDTO", bindingResult);
            return "redirect:/administration/parts/create";
        }

        if (bikePartService.checkIfPartTitleAlreadyExists(partCreateDTO.getTitle().trim())) {
            redirectAttributes.addFlashAttribute("partCreateDTO", partCreateDTO);
            redirectAttributes.addFlashAttribute("partAlreadyExists", true);
            return "redirect:/administration/parts/create";
        }

        bikePartService.savePart(partCreateDTO);
        return "redirect:/parts/all";
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
