# This configuration is loaded in every script. It defines font settings and the file
# file ending

text_size <- 22
text_family <- "Linux Libertine"
file_ending <- ".pdf" # file ending of the charts; extension defines the type

set_breaks = function(limits) {
    from <- limits[1]
    to <- limits[2]
    length <- 3
    round(seq(from, to, length.out = length))
}