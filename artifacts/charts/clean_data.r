# Remove cancelled runs from dataset

gen_df <- function(in_path, out_path) {
  df  <- read.csv(in_path, sep=",")
  clean_df <- df[(is.na(df$cancelledAt) | df$cancelledAt==""),]
  write.csv(clean_df,out_path, row.names = FALSE)
}

gen_df('data/realWorldSchemas/results.csv', 'data/realWorldSchemas/clean_data.csv')
gen_df('data/kubernetes/results.csv', 'data/kubernetes/clean_data.csv')
gen_df('data/snowplow/results.csv', 'data/snowplow/clean_data.csv')
gen_df('data/wp/results.csv', 'data/wp/clean_data.csv')
